package th.co.prior.lab1.adventureshops.service;


import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.List;

public interface InboxService {
    ApiResponse<List<InboxEntity>> getAllInboxes();

    void addInbox(Integer id, String message);

}
