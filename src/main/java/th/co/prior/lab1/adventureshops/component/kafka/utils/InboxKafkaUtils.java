package th.co.prior.lab1.adventureshops.component.kafka.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.component.kafka.component.KafkaProducerComponent;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;


import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class InboxKafkaUtils {

    @NonNull
    private KafkaProducerComponent kafkaTemplate;

    @NonNull
    private ObjectMapper mapper;

    private Random random;

    private List<PartitionInfo> partitionInfos;

    @Value("${app.config.kafka.topic}")
    private String topic;

    @PostConstruct
    public void init() {
        this.random = new Random();
        this.partitionInfos = this.kafkaTemplate.getKafkaTemplate().partitionsFor(topic);
    }

    public void execute(InboxEntity inbox) {
        try {
            String req = this.mapper.writeValueAsString(inbox);
            int nextPartition = this.generateSizePartition();
            log.info("partition: {}", nextPartition);

            this.kafkaTemplate.send(topic, nextPartition, null, req);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
        }
    }

    public int generateSizePartition() {
        int num;
        int index = this.random.nextInt((this.partitionInfos.size()));
        num = this.partitionInfos.get(index).partition();
        return num;
    }
}