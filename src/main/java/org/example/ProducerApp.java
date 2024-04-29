package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerApp {
    public static void main(String[] args) {
        ProducerApp producerApp = new ProducerApp();
        producerApp.sendMessage();
    }

    public void sendMessage() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("GROUP_ID_CONFIG", "test-group-1");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        int i = 0;
        while(true){
            ProducerRecord<String, String> message = new ProducerRecord<>("test-topic", Integer.toString(i));
            producer.send(message);
            try {
                i++;
                System.out.println("message" + i);
                Thread.sleep(1000);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
