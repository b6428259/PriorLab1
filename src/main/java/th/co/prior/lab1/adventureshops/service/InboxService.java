package th.co.prior.lab1.adventureshops.service;


import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InboxModel;

import java.util.List;

public interface InboxService {
    ApiResponse<List<InboxModel>> getAllInbox();

    ApiResponse<InboxModel> getInboxById(Integer id);

    ApiResponse<InboxModel> createInbox(Integer characterId, String message);

    ApiResponse<InboxModel> updateInbox(Integer id, String message);

}
