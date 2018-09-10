package com.netx.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class KafkaApplication {
    private static Logger logger = LoggerFactory.getLogger(KafkaApplication.class);

    private static final String TOPIC = "netx-worth";
    private static final int THREAD_AMOUNT = 1;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");//消费者的组id
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(TOPIC));

        try {
            for(;;){
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
                    JSONObject jsonObject = (JSONObject)JSON.parse(record.value());
                }
                Thread.sleep(1000 * 3);
            }
        }catch (Exception ex) {
            logger.error("consumer kafka data failed", ex);
        }
        finally {
            consumer.close();
        }

    }
}
