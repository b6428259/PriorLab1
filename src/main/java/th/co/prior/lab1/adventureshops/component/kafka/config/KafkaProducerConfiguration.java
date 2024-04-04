package th.co.prior.lab1.adventureshops.component.kafka.config;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value("${app.config.kafka.server}")
    private String bootstrapServers; // Should be the Docker service name, e.g., redpanda-0:9092

    @Value("${app.config.kafka.topic}")
    private String topic;

    @Value("${app.config.kafka.partitions:0}")
    private int partition;

    @Bean
    public ProducerFactory<String, String> settingProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean("registerKafkaTemplate")
    public KafkaTemplate<String, String> registerKafkaTemplate() {
        return new KafkaTemplate<>(settingProducerConfigs());
    }

    @Bean
    public KafkaAdmin settingTopicConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic deliciousTopic() {
        return TopicBuilder.name(topic)
                .build();
    }
}