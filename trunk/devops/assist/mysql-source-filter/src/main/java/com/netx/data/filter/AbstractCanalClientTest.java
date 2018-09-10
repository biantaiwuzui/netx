package com.netx.data.filter;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.impl.ClusterCanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.netx.data.filter.message.SourceChangeMessage;
import org.apache.commons.lang.SystemUtils;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 测试基类
 * 
 * @author jianghang 2013-4-15 下午04:17:12
 * @version 1.0.4
 */
public class AbstractCanalClientTest {

    protected final static Logger             logger             = LoggerFactory.getLogger(AbstractCanalClientTest.class);
    protected static final String             SEP                = SystemUtils.LINE_SEPARATOR;
    protected static final String             DATE_FORMAT        = "yyyy-MM-dd HH:mm:ss";
    protected volatile boolean                running            = false;
    private volatile boolean                  waiting            = true;
    protected Thread.UncaughtExceptionHandler handler            = new Thread.UncaughtExceptionHandler() {

                                                                     public void uncaughtException(Thread t, Throwable e) {
                                                                         logger.error("parse events has an error", e);
                                                                     }
                                                                 };
    protected Thread                          thread             = null;
    protected CanalConnector                  connector;
    protected static String                   context_format     = null;
    protected static String                   row_format         = null;
    protected static String                   transaction_format = null;
    protected String                          destination;

    protected Properties properties = null;
    protected Producer<String, SourceChangeMessage> producer = null;
    protected static final String SCTOPIC = "ES_DATA";

    static {
        context_format = SEP + "****************************************************" + SEP;
        context_format += "* Batch Id: [{}] ,count : [{}] , memsize : [{}] , Time : {}" + SEP;
        context_format += "* Start : [{}] " + SEP;
        context_format += "* End : [{}] " + SEP;
        context_format += "****************************************************" + SEP;

        row_format = SEP
                     + "----------------> binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {}({}) , delay : {} ms"
                     + SEP;

        transaction_format = SEP + "================> binlog[{}:{}] , executeTime : {}({}) , delay : {}ms" + SEP;

    }

    public AbstractCanalClientTest(String destination){
        this(destination, null);
    }

    public AbstractCanalClientTest(String destination, CanalConnector connector){
        this.destination = destination;
        this.connector = connector;

        properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", JsonSerializer.class);
        properties.put("acks", "all");
        properties.put("retries", 1);

        producer = new KafkaProducer<String, SourceChangeMessage>(properties);
    }

    protected void start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(new Runnable() {

            public void run() {
                process();
            }
        });

        thread.setUncaughtExceptionHandler(handler);
        running = true;
        thread.start();
    }

    protected void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (waiting) {
            if (connector instanceof ClusterCanalConnector) {
                ((ClusterCanalConnector) connector).setRetryTimes(-1);
            }
            thread.interrupt();
        }
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }

        MDC.remove("destination");
    }

    protected void process() {
        int batchSize = 5 * 1024;
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe();
                waiting = false;
                while (running) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                         try {
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {}
                    } else {
                        sendEntrys(message.getEntries());
                    }

                    connector.ack(batchId); // 提交确认
                    // connector.rollback(batchId); // 处理失败, 回滚数据
                }
            } catch (Exception e) {
                logger.error("process error!", e);
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    protected void sendEntrys(List<Entry> entrys) {
        for (Entry entry : entrys) {
            long executeTime = entry.getHeader().getExecuteTime();
            long delayTime = new Date().getTime() - executeTime;
            Date date = new Date(entry.getHeader().getExecuteTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN) {
                    TransactionBegin begin = null;
                    try {
                        begin = TransactionBegin.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务头信息，执行的线程id，事务耗时
                    logger.info(transaction_format,
                        new Object[] { entry.getHeader().getLogfileName(),
                                String.valueOf(entry.getHeader().getLogfileOffset()),
                                String.valueOf(entry.getHeader().getExecuteTime()), simpleDateFormat.format(date),
                                String.valueOf(delayTime) });
                    logger.info(" BEGIN ----> Thread id: {}", begin.getThreadId());
                } else if (entry.getEntryType() == EntryType.TRANSACTIONEND) {
                    TransactionEnd end = null;
                    try {
                        end = TransactionEnd.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务提交信息，事务id
                    logger.info("----------------\n");
                    logger.info(" END ----> transaction id: {}", end.getTransactionId());
                    logger.info(transaction_format,
                        new Object[] { entry.getHeader().getLogfileName(),
                                String.valueOf(entry.getHeader().getLogfileOffset()),
                                String.valueOf(entry.getHeader().getExecuteTime()), simpleDateFormat.format(date),
                                String.valueOf(delayTime) });
                }

                continue;
            }

            if (entry.getEntryType() == EntryType.ROWDATA) {
                RowChange rowChage = null;
                try {
                    rowChage = RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }
                sendMessage(entry, rowChage);
            }
        }
    }

    private void sendToQueue(final SourceChangeMessage scMessage) {
        producer.send(new ProducerRecord<String, SourceChangeMessage>(SCTOPIC, scMessage), new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    logger.error("失败数据:"+scMessage);
                    logger.error("发送失败", e);
                }
            }
        });
    }

    private void sendMessage(Entry entry, RowChange rowChange){
        EventType eventType = rowChange.getEventType();
        CanalEntry.Header header = entry.getHeader();
        for(RowData rowData: rowChange.getRowDatasList()) {
            if (header.getTableName().equals("value")) {
                SourceChangeMessage scMessage = null;
                if(eventType == EventType.DELETE) {
                    scMessage = fetchMessage(header, eventType, rowData.getBeforeColumnsList());
                } else{
                    scMessage = fetchMessage(header, eventType, rowData.getAfterColumnsList());
                }
                sendToQueue(scMessage);
            }
        }
    }
    private SourceChangeMessage fetchMessage(CanalEntry.Header header, EventType eventType, List<Column> columns){
        SourceChangeMessage scMessage = new SourceChangeMessage();
        scMessage.setDbName(header.getSchemaName());
        scMessage.setTableName(header.getTableName());
        scMessage.setEventType(eventType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(Column column: columns) {
            if (column.getName().equals("id")) {
                scMessage.setPkId(column.getValue());
            }
            if (column.getName().equals("create_time")) {
                Date createTime = new Date();
                try{
                    createTime = sdf.parse(column.getValue());
                } catch (Exception e){

                }
                scMessage.setCreateTime(createTime);
            }
            if (column.getName().equals("update_time")) {
                Date updateTime = new Date();
                try{
                    updateTime = sdf.parse(column.getValue());
                } catch (Exception e){

                }
                scMessage.setUpdateTime(updateTime);
            }
            if (column.getName().equals("deleted")) {
                scMessage.setDeleted(!column.getValue().equals("0"));
            }
        }
        return scMessage;
    }

    protected void printColumn(List<Column> columns) {
        for (Column column : columns) {
            StringBuilder builder = new StringBuilder();
            builder.append(column.getName() + " : " + column.getValue());
            builder.append("    type=" + column.getMysqlType());
            if (column.getUpdated()) {
                builder.append("    update=" + column.getUpdated());
            }
            builder.append(SEP);
            logger.info(builder.toString());
        }
    }

    public void setConnector(CanalConnector connector) {
        this.connector = connector;
    }

}
