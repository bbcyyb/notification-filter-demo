package com.dellemc.katalist.notificationfilter.output;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Output;
import kafka.Kafka;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaOutput extends Output {

    private Producer<String, String> producer;
    private String topic;

    @Override
    public void init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.62.59.210:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        topic = "filteredEvent0";
        producer = new KafkaProducer<>(props);
        if(logger.isDebugEnabled()) {
            logger.debug("Initialize KafkaOutput, bootstrap.servers is {}", props.getProperty("bootstrap.servers"));
        }
    }

    @Override
    protected void doProcess(Map<String, Object> event, Context context) {
        String value = generateMessage(event, context);
        logger.info("Start sending message \"{}\" to topic [{}] ....", value, topic);
        ProducerRecord<String, String> message = new ProducerRecord<>(topic, value);
        producer.send(message);
    }

    @Override
    public void dispose() {
        producer.close(100, TimeUnit.MILLISECONDS);
    }

    private String generateMessage(Map<String, Object> event, Context context) {
        return String.format("%s %s %s",
                event.get("node").toString(), event.get("metric").toString(), event.get("value").toString());
    }
}
