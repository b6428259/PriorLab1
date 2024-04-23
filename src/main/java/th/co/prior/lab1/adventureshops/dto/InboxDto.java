package th.co.prior.lab1.adventureshops.dto;



import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.InboxModel;
import th.co.prior.lab1.adventureshops.repository.InboxRepository;


import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InboxDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboxDto.class);
    private final EntityDto entityDTO;
    private final PlayerDto playerDTO;
    private final InboxRepository inboxRepository;

    public List<InboxModel> toDTOList(List<InboxEntity> inbox) {
        return inbox.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public InboxModel toDTO(InboxEntity inbox) {
        InboxModel dto = new InboxModel();
        dto.setId(inbox.getId());
        dto.setRecipient(inbox.getPlayer().getName());
        dto.setMessage(inbox.getMessage());

        return dto;
    }

    public List<InboxEntity> findAllInbox() {
        return inboxRepository.findAll();
    }

    public InboxEntity findInboxById(Integer id) {
        return inboxRepository.findById(id).orElse(null);
    }

    public void addInbox(Integer id, String message) {
        try {
            PlayerEntity character = this.playerDTO.findPlayerById(id);

            if (this.entityDTO.hasEntity(character)) {
                InboxEntity inbox = new InboxEntity();
                inbox.setPlayer(character);
                inbox.setMessage(message);
                this.inboxRepository.save(inbox);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }


}