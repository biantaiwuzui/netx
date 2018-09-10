package com.netx.extrator.kafka;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.netx.extrator.util.JsonUtil;
import com.netx.extrator.util.PropsUtil;

import java.util.*;
import java.util.regex.Pattern;

import static com.netx.extrator.util.CollectionUtil.isEmpty;

/**
 * kafka生产者
 * Created by Yawn on 2018/7/18
 */
public class CanalKafkaProducer extends AbstractCanalKafKaProducer{
    private static final Logger logger = LoggerFactory.getLogger(CanalKafkaProducer.class);
    private KafkaProducer<String, String> producer;
    private int count;


    public CanalKafkaProducer(String config) {
        Properties properties = PropsUtil.loadProps(config);
        producer = new KafkaProducer(properties);
        count = 0;
    }

    public int getCount() {
        return count;
    }

    public void stop() {
        try {
            logger.info("## stop the kafka producer");
            producer.close();
        } catch (Throwable e) {
            logger.warn("##something goes wrong when stopping kafka producer:", e);
        } finally {
            logger.info("## kafka producer is down.");
        }
    }



    public void extratorAndSend(Message message) {
        extractEntry(message.getEntries());
    }

    private void extractEntry(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            Map<String, String> entryMap = new HashMap<String, String>(60);
            Map<String, String> keyMap = new HashMap<String, String>(3);
            //这个情况跳过
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                logger.error("ERROR ## parser of eromanga-event has an error , data:" + entry.toString());
                continue;
            }
            CanalEntry.EventType eventType = rowChage.getEventType();
            String tableName = entry.getHeader().getTableName();
            List<String> tableList = Arrays.asList(WORTH_TABLES);
            // 开始过滤数据
             if (tableList.contains(tableName)) {
                 keyMap.put("table_name", tableName);
                 keyMap.put("action", eventType.toString());
                 //每一行数据
                 for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                     extractAndSend(eventType, rowData, entryMap, keyMap);
                 }
             } else if (Pattern.matches(RELATED_TABLES_PATTERN, tableName)) {
                 keyMap.put("action", "UPDATE");
                 keyMap = selectTableName(tableName, keyMap);
                 for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                     if (eventType == CanalEntry.EventType.DELETE) {
                         entryMap = extractIdToMap(rowData.getBeforeColumnsList(), entryMap);
                         // 有一些相关表格没有相关ID，就退出
                         if (isEmpty(entryMap))
                             continue;
                         sendToKafka(keyMap, entryMap);
                     } else if (eventType == CanalEntry.EventType.INSERT) {
                         entryMap = extractIdToMap(rowData.getAfterColumnsList(), entryMap);
                         if (isEmpty(entryMap))
                             continue;
                         sendToKafka(keyMap, entryMap);
                     } else {
                         entryMap = extractIdToMap(rowData.getAfterColumnsList(), entryMap);
                         if (isEmpty(entryMap))
                             continue;
                         sendToKafka(keyMap, entryMap);
                     }
                 }
             }
        }
    }

    private Map<String, String> selectTableName(String tableName, Map<String, String> keyMap) {
        if(Pattern.matches(DEMAND_RELATED_TABLES_PATTERN, tableName)) {
            keyMap.put("table_name", "demand");
        } else if(Pattern.matches(SKILL_RELATED_TABLES_PATTERN, tableName)) {
            keyMap.put("table_name", "skill");
        } else if(Pattern.matches(WISH_RELATED_TABLES_PATTERN, tableName)) {
            keyMap.put("table_name", "wish");
        } else if(Pattern.matches(MEETING_RELATED_TABLES_PATTERN, tableName)) {
            keyMap.put("table_name", "meeting");
        }
        return keyMap;
    }

    private void extractAndSend(CanalEntry.EventType eventType, CanalEntry.RowData rowData, Map<String, String> entryMap, Map<String, String> keyMap) {
        if (eventType == CanalEntry.EventType.DELETE) {
            --count;
            entryMap = extractMessageToMap(rowData.getBeforeColumnsList(), entryMap);
            sendToKafka(keyMap, entryMap);
        } else if (eventType == CanalEntry.EventType.INSERT) {
            ++count;
            entryMap = extractMessageToMap(rowData.getAfterColumnsList(), entryMap);
            sendToKafka(keyMap, entryMap);
        } else {
            entryMap = extractMessageToMap(rowData.getAfterColumnsList(), entryMap);
            sendToKafka(keyMap, entryMap);
        }
    }


    private void sendToKafka(Map<String, String> keyMap, Map<String, String> entryMap) {
        String jsonString = JsonUtil.map2Josn(entryMap);
        keyMap.put("time", String.valueOf(System.currentTimeMillis()));
        String jsonKey = JsonUtil.map2Josn(keyMap);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(
                TOPIC, jsonKey, jsonString);
        producer.send(producerRecord);
    }


    private Map<String, String> extractIdToMap(List < CanalEntry.Column> columnsList, Map<String, String> entryMap) {
        for (CanalEntry.Column column : columnsList) {
            //只提起ID字段
            if (Pattern.matches(TABLE_ID, column.getName())) {
                entryMap.put("id", column.getValue());
            }
        }
        return entryMap;
    }

    private Map<String, String> extractMessageToMap (List < CanalEntry.Column> columnsList, Map < String, String > entryMap){
        List<String> colums = Arrays.asList(COLUM_LIST);
        //只提起相关字段
        for (CanalEntry.Column column : columnsList) {
            if (colums.contains(column.getName())) {
                entryMap.put(column.getName(), column.getValue());
            }
        }
        return entryMap;
    }
}
