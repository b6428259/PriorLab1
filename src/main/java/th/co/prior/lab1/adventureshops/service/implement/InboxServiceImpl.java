package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.repository.InboxRepository;
import th.co.prior.lab1.adventureshops.service.InboxService;


import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class InboxServiceImpl implements InboxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboxServiceImpl.class);
    private final InboxRepository inboxRepository;
    private final PlayerServiceImpl playerService;

    @Override
    public ApiResponse<List<InboxEntity>> getAllInboxes() {
        ApiResponse<List<InboxEntity>> result = new ApiResponse<>();
        try {
            List<InboxEntity> inboxes = this.inboxRepository.findAll();
            if (!inboxes.isEmpty()) {
                result.setStatus(200);
                result.setDescription("List of all Inbox retrieved successfully.");
                result.setData(inboxes);
            } else {
                result.setStatus(404);
                result.setDescription("No Inbox Found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }


    @Override
    public void addInbox(Integer id, String message) {
        try {
            ApiResponse<PlayerEntity> playerResponse = playerService.getPlayerById(id);
            PlayerEntity character = playerResponse.getData();
            if (Objects.nonNull(character)) {
                InboxEntity inbox = new InboxEntity();
                inbox.setPlayer(character);
                inbox.setMessage(message);
                this.inboxRepository.save(inbox);
            }
        } catch (Exception e) {
            LOGGER.error("Error adding inbox for player with id {}: {}", id, e.getMessage());
        }
    }
}
