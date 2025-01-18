package writemessage;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class KafkaOrderProducer {

    public static void main(String[] args) {
        // 1. 配置 Kafka 生产者
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Kafka Broker 地址
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 确保消息可靠性：等待所有副本确认
        props.put("acks", "all");
        // 设置重试次数（如果消息发送失败）
        props.put("retries", 3);
        // 批量发送的大小（字节）
        props.put("batch.size", 16384);
        // 消息延迟时间（等待更多消息一起发送）
        props.put("linger.ms", 1);
        // 生产者缓冲区内存大小
        props.put("buffer.memory", 33554432);

        // 2. 创建 KafkaProducer 实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 定义主题名称
        String topic = "order-topic";

        // 3. 模拟发送订单消息
        try {
            for (int i = 0; i < 10; i++) {
                // 生成随机订单信息
                String orderId = UUID.randomUUID().toString(); // 唯一订单 ID
                String userId = "user-" + (new Random().nextInt(100) + 1); // 模拟用户 ID
                double orderAmount = new Random().nextDouble() * 1000; // 随机订单金额

                // 构造订单消息（JSON 格式）
                String orderMessage = String.format(
                        "{\"orderId\":\"%s\", \"userId\":\"%s\", \"amount\":%.2f}",
                        orderId, userId, orderAmount
                );

                // 发送消息
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, userId, orderMessage);
                RecordMetadata metadata = producer.send(record).get();

                // 打印消息发送的元数据信息
                System.out.printf("Sent message: key=%s, value=%s, topic=%s, partition=%d, offset=%d%n",
                        userId, orderMessage, metadata.topic(), metadata.partition(), metadata.offset());

                // 模拟发送间隔
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭生产者
            producer.close();
        }
    }
}