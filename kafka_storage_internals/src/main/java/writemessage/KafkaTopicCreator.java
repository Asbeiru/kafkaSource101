package writemessage;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;

public class KafkaTopicCreator {

    public static void main(String[] args) {
        // 配置 Kafka AdminClient
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            // 定义主题名称
            String topicName = "word-count-output";

            // 检查主题是否存在
            if (!adminClient.listTopics().names().get().contains(topicName)) {
                // 创建主题
                NewTopic newTopic = new NewTopic(topicName, 3, (short) 1); // 3 分区，1 副本
                adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
                System.out.println("Topic created: " + topicName);
            } else {
                System.out.println("Topic already exists: " + topicName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}