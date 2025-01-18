package stream.wordcount;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaStreamWordCountConsumer {

    public static void main(String[] args) {
        // 配置消费者
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // Kafka 集群地址
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "wordcount-consumer-group"); // 消费者组 ID
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // Key 反序列化
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName()); // Value 反序列化
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 从最早的偏移量开始消费

        // 创建 Kafka 消费者
        KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(properties);

        // 订阅主题
        consumer.subscribe(Collections.singletonList("word-count-output"));

        System.out.println("Listening to topic: word-count-output");

        try {
            // 无限循环监听消息
            while (true) {
                // 拉取消息，设置超时时间为 1 秒
                ConsumerRecords<String, Long> records = consumer.poll(Duration.ofSeconds(1));

                // 遍历每条消息并打印
                records.forEach(record -> {
                    System.out.printf("Consumed message: Key = %s, Value = %d, Partition = %d, Offset = %d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                });
            }
        } catch (Exception e) {
            System.err.println("Error while consuming messages: " + e.getMessage());
        } finally {
            // 关闭消费者
            consumer.close();
        }
    }
}