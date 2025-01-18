package stream.wordcount;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

public class KafkaStreamWordCountProducer {

    public static void main(String[] args) {
        // 配置生产者
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // Kafka 集群地址
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Key 序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Value 序列化

        // 创建 Kafka 生产者
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // 提示用户输入数据
        System.out.println("Enter text lines to send to Kafka (type 'exit' to quit):");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                // 从控制台读取输入
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                // 创建 ProducerRecord 并发送消息
                ProducerRecord<String, String> record = new ProducerRecord<>("word-count-input", null, line);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        // 打印消息元数据
                        System.out.printf("Message sent to topic: %s, partition: %d, offset: %d%n",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    } else {
                        // 打印异常
                        System.err.println("Error while producing message: " + exception.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error while running producer: " + e.getMessage());
        } finally {
            // 关闭生产者
            producer.close();
        }
    }
}