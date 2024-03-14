package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;

import java.util.List;

public interface InventoryService {

    ResponseModel<List<InventoryEntity>> getAllInventories();

    ResponseModel<InventoryEntity> getInventoryById(Integer id);

    void addInventory(String name, Integer playerId, Integer monsterId);

}
