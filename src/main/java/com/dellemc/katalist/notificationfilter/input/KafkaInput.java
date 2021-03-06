package com.dellemc.katalist.notificationfilter.input;

import com.dellemc.katalist.notificationfilter.Context;
import com.dellemc.katalist.notificationfilter.base.Input;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.dellemc.katalist.notificationfilter.decoder.Decode;
import com.dellemc.katalist.notificationfilter.decoder.JsonDecoder;
import com.dellemc.katalist.notificationfilter.decoder.PlainDecoder;
import com.dellemc.katalist.notificationfilter.job.JobStatusEnum;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaInput extends Input {

    private ExecutorService executor;
    private Map<String, Integer> topics;
    private Properties props;
    private ArrayList<ConsumerThread> consumerThreadsList;

    @Override
    public void init() {
        topics = new HashMap<>();
        topics.put("event0", 1);

        props = new Properties();
        props.setProperty("bootstrap.servers", "10.62.59.210:9092");
        props.setProperty("group.id", "subscriber");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumerThreadsList = new ArrayList<>();
    }

    @Override
    protected void doProcess(Map<String, Object> event, Context context) {
        this.getFilterProcessor().process(event, context);
    }

    @Override
    public void emit() {
        topics.entrySet().stream().forEach(entry -> {
            String topic = entry.getKey();
            int threadSettingSize = entry.getValue();
            KafkaConsumer consumer = new KafkaConsumer<>(props);
            int partitionSize = consumer.partitionsFor(topic).size();
            int threadSize = partitionSize < threadSettingSize ? partitionSize : threadSettingSize;
            executor = Executors.newFixedThreadPool(threadSize);
            for (int i = 0; i < threadSize; i++) {
                ConsumerThread thread = new ConsumerThread(topic, props);
                consumerThreadsList.add(thread);
                executor.submit(thread);
            }
        });
    }

    @Override
    public void dispose() {
        consumerThreadsList.forEach(consumerThread -> consumerThread.dispose());
        executor.shutdown();
        try {
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            logger.error("InterruptedException: ", ex);
        }
    }

    private Context generateContext(long lastCommittedOffset, int partition) {
        return new Context(lastCommittedOffset, JobStatusEnum.Created, partition);
    }

    private class ConsumerThread implements Runnable {

        private KafkaConsumer<String, String> consumer;
        private Decode decoder;

        public ConsumerThread(String topicName, Properties props) {
            consumer = new KafkaConsumer<>(props);
            initConsumerThread(props);
            consumer.subscribe(Arrays.asList(topicName));
            decoder = new JsonDecoder();
        }

        public void initConsumerThread(Properties props) {
            consumer = new KafkaConsumer<>(props);
        }

        public void run() {
            topics.forEach((topic, partition) -> logger.info("Start listening Kafka Topic {}, partition {} ....", topic, partition));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                for (ConsumerRecord<String, String> record : records) {
                    Context context = generateContext(record.offset(), record.partition());
                    process(decoder, record.value(), context);
                }
            }
        }

        public void dispose() {
            consumer.wakeup();
            consumer.close();
        }
    }
}
