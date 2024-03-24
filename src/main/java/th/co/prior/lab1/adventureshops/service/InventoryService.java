package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;

import java.util.List;

public interface InventoryService {

    ApiResponse<List<InventoryEntity>> getAllInventories();

//    ApiResponse<InventoryModel> getInventoryByName(String name);

    ApiResponse<InventoryModel> getInventoryById(Integer id);

    void addInventory(String name, Integer playerId, Integer monsterId);

}
