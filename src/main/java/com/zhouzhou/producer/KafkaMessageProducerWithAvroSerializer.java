package com.zhouzhou.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaMessageProducerWithAvroSerializer {

    public static void main(String[] args) {
        // Kafka broker 地址
        String bootstrapServers = "localhost:9092";  // 修改为实际的 Kafka 服务器地址
        String topicName = "demoTopic";           // 要发送消息的主题名称

        // 配置 Kafka Producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 创建 KafkaProducer 实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // ------------------

        // 发送消息
        try {
            for (int i = 0; i < 10; i++) {
                String key = "key-" + i;
                String value = "message-周远+张晓琴" + i;

                // 创建 ProducerRecord 对象
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);

                // 同步发送消息并获得元数据
                RecordMetadata metadata = producer.send(record).get();

                System.out.printf("Sent record(key=%s value=%s) meta(partition=%d, offset=%d)\n",
                        record.key(), record.value(), metadata.partition(), metadata.offset());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }



}