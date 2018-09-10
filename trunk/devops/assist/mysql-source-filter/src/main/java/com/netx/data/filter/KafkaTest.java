package com.netx.data.filter;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * Created by CloudZou on 5/12/18.
 */
public class KafkaTest {

    public static void main(String[] args) throws Exception{
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("request.timeout.ms", "60000");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("acks", "all");
        properties.put("retries", 1);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for(int i=10; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("TOPIC222sss", "Hello world: "+ i), new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            Thread.sleep(1* 1000);
        }
    }
}
