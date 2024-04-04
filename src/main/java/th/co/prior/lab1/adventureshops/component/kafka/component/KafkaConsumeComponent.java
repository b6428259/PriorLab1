//package th.co.prior.lab1.adventureshops.component.kafka.component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//import th.co.prior.lab1.adventureshops.model.InboxModel;
//import th.co.prior.lab1.adventureshops.repository.InboxRepository;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class KafkaConsumeComponent {
//
//    @Qualifier("mapper")
//    @NonNull
//    private final ObjectMapper mapper;
//
//    @NonNull
//    private final InboxRepository inboxRepository;
//
//    @KafkaListener(topics = "${app.config.kafka.topic}", groupId = "${app.config.kafka.group}")
//    public void consumerMessage(@Payload String message) {
//        log.info("Received message: {}", message);
//
//        try {
//            InboxModel inboxModel = mapper.readValue(message, InboxModel.class);
//            inboxRepository.insertInbox(inboxModel);
//            log.info("Message processed successfully: {}", message);
//        } catch (Exception e) {
//            log.error("Error processing message: {}", e.getMessage(), e);
//        }
//    }
//}
