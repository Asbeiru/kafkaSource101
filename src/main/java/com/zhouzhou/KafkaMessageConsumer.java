package com.zhouzhou;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class KafkaMessageConsumer {

    public static void main(String[] args) {
        // Kafka broker 地址
        String bootstrapServers = "localhost:9092";  // 修改为实际的 Kafka 服务器地址
        String topicName = "demoTopic";           // 要消费的主题名称
        String groupId = "my-group";                 // 消费者所属的组 ID

        // 配置 Kafka Consumer
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // 从最早的消息开始消费

        // 创建 KafkaConsumer 实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // 订阅主题
        consumer.subscribe(Collections.singletonList(topicName));

        // 消费消息
        try {
            while (true) {
                // 拉取消息，等待最多 1 秒
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed record(key=%s value=%s) meta(partition=%d, offset=%d)\n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
            }
        } finally {
            consumer.close();
        }


        // ----------------------------







    }
}