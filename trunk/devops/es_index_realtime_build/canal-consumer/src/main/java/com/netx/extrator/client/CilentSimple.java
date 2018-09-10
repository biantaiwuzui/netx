package com.netx.extrator.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.netx.extrator.kafka.CanalKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Yawn on 2018/7/18 0018.
 */
public class CilentSimple {
    private static final Logger logger = LoggerFactory.getLogger(CanalKafkaProducer.class);
    //抓取数据的大小
    private static final int BATCHSIZE = 300;
    private CanalKafkaProducer kafkaProducer;
    private CanalConnector connector;

    public CilentSimple(CanalKafkaProducer kafkaProducer, CanalConnector connector) {
        this.kafkaProducer = kafkaProducer;
        this.connector = connector;
    }

    public void work() {
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            boolean running = true;
            while (running) {
                Message message = connector.getWithoutAck(BATCHSIZE); // 获取指定数量的数据
                long batchId = message.getId();
                try {
                    int size = message.getEntries().size();
                    if (batchId != -1 && size != 0) {
                        kafkaProducer.extratorAndSend(message); // 发送message到所有topic
                        connector.ack(batchId); // 提交确认
                    } else {
                        try {
                            Thread.sleep(1000);
                            logger.info("had received ...." + kafkaProducer.getCount()+"count");
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    connector.rollback(batchId); // 处理失败, 回滚数据
                }
            }
        } finally {
            kafkaProducer.stop();
            connector.disconnect();
        }
    }


}
