package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.InboxDto;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InboxModel;
import th.co.prior.lab1.adventureshops.model.PlayerModel;
import th.co.prior.lab1.adventureshops.repository.InboxRepository;
import th.co.prior.lab1.adventureshops.service.InboxService;


import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final InboxRepository inboxRepository;
    private final InboxDto inboxDto;

    @Override
    public ApiResponse<List<InboxModel>> getAllInbox() {
        ApiResponse<List<InboxModel>> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<InboxEntity> inboxes = this.inboxRepository.findAll();

            if (inboxes.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved inboxes information.");
                result.setData(this.inboxDto.toDTOList(inboxes));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            result.setDescription("Inbox not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<InboxModel> getInboxById(Integer id) {
        ApiResponse<InboxModel> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            InboxEntity inboxes = this.inboxRepository.findById(id).orElseThrow(() -> new NullPointerException("Inbox not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved inboxes information.");
            result.setData(this.inboxDto.toDTO(inboxes));
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<InboxModel> createInbox(Integer characterId, String message) {
        return null;
    }

    @Override
    public ApiResponse<InboxModel> updateInbox(Integer id, String message) {
        return null;
    }
}