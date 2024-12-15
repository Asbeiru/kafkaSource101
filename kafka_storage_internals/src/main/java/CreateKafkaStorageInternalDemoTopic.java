import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateKafkaStorageInternalDemoTopic {
    public static void main(String[] args) {
        // Kafka 集群的 bootstrap.servers 地址
        String bootstrapServers = "localhost:9092";  // 修改为你的 Kafka 集群地址

        // 配置 Kafka AdminClient
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // 创建 AdminClient 实例
        try (AdminClient adminClient = AdminClient.create(config)) {
            // 创建一个新的 Kafka 主题
            String topicName = "kafka-storage-internals-topic";  // 要创建的主题名称
            int partitions = 3;                 // 分区数
            short replicationFactor = 1;        // 副本因子（在 KRaft 模式中可以为1）

            // 创建 NewTopic 对象
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

            // 使用 AdminClient 创建主题
            adminClient.createTopics(Collections.singleton(newTopic)).all().get(3, TimeUnit.SECONDS);

            System.out.println("Topic '" + topicName + "' created successfully!");

        } catch (ExecutionException e) {
            System.err.println("Error creating topic: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while creating topic: " + e.getMessage());
        } catch (TimeoutException e) {
            System.err.println("TimeoutException: " + e.getMessage());
        }
    }
}
