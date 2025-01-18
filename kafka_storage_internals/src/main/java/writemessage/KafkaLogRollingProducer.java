package writemessage;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.UUID;

public class KafkaLogRollingProducer {

    public static void main(String[] args) {
        // 1. 配置 Kafka 生产者
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Kafka Broker 地址
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all"); // 确保消息可靠性
        props.put("batch.size", 65536); // 批量发送大小（64KB）
        props.put("linger.ms", 10); // 消息发送延迟（10ms）
        props.put("buffer.memory", 33554432); // 缓冲区大小（32MB）

        // 2. 创建 KafkaProducer 实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 定义主题名称
        String topic = "log-rolling-topic";

        // 3. 快速发送大量数据
        try {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 50000; i++) { // 大量发送消息
                String key = "key-" + i;
                String value = generateLargeMessage(i);

                // 发送消息
                RecordMetadata metadata = producer.send(new ProducerRecord<>(topic, key, value)).get();

                // 打印消息发送元数据
                System.out.printf("Sent message: key=%s, topic=%s, partition=%d, offset=%d%n",
                        key, metadata.topic(), metadata.partition(), metadata.offset());

                // 每隔一段时间打印运行时间
                if (i % 1000 == 0) {
                    System.out.printf("Sent %d messages, elapsed time: %d ms%n", i,
                            System.currentTimeMillis() - startTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭生产者
            producer.close();
        }
    }

    // 生成较大的消息内容（模拟实际业务数据）
    private static String generateLargeMessage(int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(UUID.randomUUID()).append("\",");
        sb.append("\"index\":").append(index).append(",");
        sb.append("\"data\":\"");
        for (int i = 0; i < 100; i++) { // 每条消息包含 100 个随机字符串
            sb.append(UUID.randomUUID().toString()).append(" ");
        }
        sb.append("\"}");
        return sb.toString();
    }
}