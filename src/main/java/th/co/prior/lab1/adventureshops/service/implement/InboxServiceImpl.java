package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setMessage("Not Found");

        try {
            List<InboxEntity> inboxes = inboxRepository.findAll();

            if (!inboxes.isEmpty()) {
                result.setStatus(HttpStatus.OK.value());
                result.setMessage("OK");
                result.setDescription("Successfully retrieved inboxes information.");
                result.setData(inboxDto.toDTOList(inboxes));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            result.setDescription("Inbox not found!");
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<InboxModel> getInboxById(Integer id) {
        ApiResponse<InboxModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setMessage("Not Found");

        try {
            InboxEntity inbox = inboxRepository.findById(id)
                    .orElseThrow(() -> new NullPointerException("Inbox not found!"));

            result.setStatus(HttpStatus.OK.value());
            result.setMessage("OK");
            result.setDescription("Successfully retrieved inbox information.");
            result.setData(inboxDto.toDTO(inbox));
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
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