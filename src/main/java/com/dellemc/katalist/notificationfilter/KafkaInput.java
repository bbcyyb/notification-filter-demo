package com.dellemc.katalist.notificationfilter;

import com.dellemc.katalist.notificationfilter.base.Input;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.dellemc.katalist.notificationfilter.decoder.Decode;
import com.dellemc.katalist.notificationfilter.decoder.PlainDecoder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaInput extends Input {

    private ExecutorService executor;
    private Map<String, Integer> topics;
    private Properties props;
    private ArrayList<ConsumerThread> consumerThreadsList;

    @Override
    protected void init() {
        topics = new HashMap<>();
        topics.put("test1", 1);
        topics.put("test2", 4);
        topics.put("test3", 6);

        props = new Properties();
        props.setProperty("bootstrap.servers", "10.62.59.210:9092");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumerThreadsList = new ArrayList<>();
    }

    @Override
    public void doProcess(Map<String, Object> event) {
        topics.entrySet().stream().forEach(entry -> {
            String topic = entry.getKey();
            int threadSettingSize = entry.getValue();
            KafkaConsumer consumer = new KafkaConsumer<>(props);
            int partitionSize = consumer.partitionsFor(topic).size();
            int threadSize = partitionSize < threadSettingSize ? partitionSize : threadSettingSize;
            executor = Executors.newFixedThreadPool(threadSize);
            for (int i = 0; i < threadSize; i++) {
                ConsumerThread thread = new ConsumerThread(topic, props, this);
                consumerThreadsList.add(thread);
                executor.submit(thread);
            }
        });
    }

    @Override
    public void shutdown() {
        consumerThreadsList.forEach(consumerThread -> consumerThread.shutdown());
        executor.shutdown();
        try {
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ConsumerThread implements Runnable {

        private KafkaConsumer<String, String> consumer;
        private Decode decoder;

        public ConsumerThread(String topicName, Properties props, KafkaInput kafka) {
            consumer = new KafkaConsumer<>(props);
            initConsumerThread(props, kafka);
            this.consumer.subscribe(Arrays.asList(topicName));
            decoder = new PlainDecoder();
        }

        public void initConsumerThread(Properties prop, KafkaInput kafka) {
            this.consumer = new KafkaConsumer<>(props);
        }

        public void run() {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                for (ConsumerRecord<String, String> record : records)
                    process(decoder, record.value());
            }
        }

        public void shutdown() {
            consumer.wakeup();
            consumer.close();
        }
    }
}
