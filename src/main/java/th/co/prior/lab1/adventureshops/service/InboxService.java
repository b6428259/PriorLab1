package th.co.prior.lab1.adventureshops.service;


import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;

import java.util.List;

public interface InboxService {
    ResponseModel<List<InboxEntity>> getAllInboxes();

    void addInbox(Integer id, String message);

}
