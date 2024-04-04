package th.co.prior.lab1.adventureshops.component.kafka.component;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducerComponent {

    @Qualifier(value = "registerKafkaTemplate")
    @NonNull
    private KafkaTemplate<String, String> kafkaTemplate;

    public @NonNull KafkaTemplate<String, String> getKafkaTemplate(){
        return this.kafkaTemplate;
    }

    public void send(@NonNull String topic, Integer partition, String key, String value) {
        this.kafkaTemplate.send(topic, partition, key, value)
                .whenComplete((result, throwable) -> {
                    if (null == throwable) {
                        log.info("kafka send to {} and partition {}"
                                ,result.getRecordMetadata().topic()
                                ,result.getRecordMetadata().partition());
                    } else {
                        log.info("kafka send exception {}", throwable.getMessage());
                    }
                });
    }
}