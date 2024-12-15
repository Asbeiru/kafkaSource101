import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SegmentRollingProducer {
    public static void main(String[] args) {
        // Kafka broker 地址
        String bootstrapServers = "localhost:9092"; // 修改为实际的 Kafka 服务器地址
        String topicName = "kafka-storage-internals-topic";            // 要发送消息的主题名称

        // 配置 Kafka Producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 创建 KafkaProducer 实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // 创建一个较大的消息体
        StringBuilder largeMessage = new StringBuilder();
        for (int j = 0; j < 1024; j++) { // 每条消息约 1KB
            largeMessage.append("A");
        }

        // 发送消息
        try {
            for (int i = 0; i < 1000; i++) { // 循环发送消息
                String key = "fixedKey"; // 固定 key，确保所有消息进入同一个分区
                String value = largeMessage.toString();

                // 创建 ProducerRecord 对象
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);

                System.out.printf("Sent record(key=%s, valueSize=%d)\n",
                        record.key(), value.length());

                // 同步发送消息并获得元数据
                RecordMetadata metadata = producer.send(record).get();

                System.out.printf("Sent record(key=%s, partition=%d, offset=%d, valueSize=%d)\n",
                        record.key(), metadata.partition(), metadata.offset(), value.length());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
