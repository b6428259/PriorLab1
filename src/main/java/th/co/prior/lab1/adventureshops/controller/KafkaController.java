package th.co.prior.lab1.adventureshops.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.lab1.adventureshops.component.kafka.component.KafkaProducerComponent;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerComponent kafkaProducerComponent;

    @Autowired
    public KafkaController(KafkaProducerComponent kafkaProducerComponent) {
        this.kafkaProducerComponent = kafkaProducerComponent;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody KafkaMessageRequest request) {
        String topic = request.getTopic();
        Integer partition = request.getPartition();
        String key = request.getKey();
        String value = request.getValue();

        kafkaProducerComponent.send(topic, partition, key, value);

        return ResponseEntity.ok("Message sent successfully");
    }

    // Define a class to represent the request body

    @Getter
    @Setter
    static class KafkaMessageRequest {
        private String topic;
        private Integer partition;
        private String key;
        private String value;

        // Getters and setters
        // ...
    }
}