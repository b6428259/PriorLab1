package th.co.prior.lab1.adventureshops.service.implement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;
import th.co.prior.lab1.adventureshops.service.InventoryService;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepository inventoryRepository;
    private final PlayerDto playerDto;
    private final MonsterDto monsterDto;
    private final InventoryDto inventoryDto;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, PlayerDto playerDto, MonsterDto monsterDto, InventoryDto inventoryDto) {
        this.inventoryRepository = inventoryRepository;
        this.playerDto = playerDto;
        this.monsterDto = monsterDto;
        this.inventoryDto = inventoryDto;
    }

    @Override
    public ApiResponse<List<InventoryEntity>> getAllInventories() {
        ApiResponse<List<InventoryEntity>> result = new ApiResponse<>();
        try {
            List<InventoryEntity> inventories = inventoryRepository.findAll();
            if (!inventories.isEmpty()) {
                result.setStatus(200);
                result.setDescription("List of all Inventories retrieved successfully.");
                result.setData(inventories);
            } else {
                result.setStatus(404);
                result.setDescription("No Inventories Found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription("Internal Server Error");
            LOGGER.error("Error fetching all inventories: {}", e.getMessage());
        }
        return result;
    }

//    @Override
//    public ApiResponse<InventoryModel> getInventoryByName(String name) {
//        ApiResponse<InventoryModel> result = new ApiResponse<>();
//        try {
//            LOGGER.info("Searching for inventory by name: {}", name);
//
//            List<InventoryEntity> inventories = inventoryRepository.findByName(name);
//
//            if (!inventories.isEmpty()) {
//                LOGGER.info("Found {} inventories with name: {}", inventories.size(), name);
//                // Assuming you want to return the first inventory found
//                InventoryEntity firstInventory = inventories.get(0);
//                InventoryModel inventoryModel = this.inventoryDto.toDTO(firstInventory);
//
//                result.setStatus(200);
//                result.setDescription("OK");
//                result.setData(inventoryModel);
//            } else {
//                result.setStatus(404);
//                result.setDescription("Inventory not found with name: " + name);
//            }
//        } catch (Exception e) {
//            result.setStatus(500);
//            result.setDescription("Internal Server Error");
//            LOGGER.error("Error fetching inventory by name: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//


    @Override
    public ApiResponse<InventoryModel> getInventoryById(Integer id) {
        ApiResponse<InventoryModel> result = new ApiResponse<>();
        try {
            InventoryEntity inventory = inventoryRepository.findById(id)
                    .orElse(null);
            if (inventory != null) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(this.inventoryDto.toDTO(inventory));
            } else {
                result.setStatus(404);
                result.setDescription("Inventory not found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription("Internal Server Error");
            LOGGER.error("Error fetching inventory by id: {}", e.getMessage());
        }
        return result;
    }

    @Override
    public void addInventory(String name, Integer playerId, Integer monsterId) {
        ApiResponse<String> result = new ApiResponse<>();
        try {
            PlayerEntity character = playerDto.findPlayerById(playerId);
            MonsterEntity monster = monsterDto.findMonsterById(monsterId);

            if (character != null && monster != null) {
                InventoryEntity inventory = new InventoryEntity();
                inventory.setName(name);
                inventory.setPlayer(character);
                inventory.setMonster(monster);
                inventoryRepository.save(inventory);

                result.setStatus(200);
                result.setDescription("OK");
                result.setData("You have successfully added inventory.");
            } else {
                result.setStatus(400);
                result.setDescription("Invalid player or monster ID provided.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription("Internal Server Error");
            LOGGER.error("Error adding inventory: {}", e.getMessage());
        }
    }


    public void changeOwner(PlayerEntity character, InventoryEntity inventory) {
        try {
            inventory.setPlayer(character);
            this.inventoryRepository.save(inventory);
        } catch (Exception e) {
            LOGGER.error("Error changing owner for inventory: {}", e.getMessage());
        }
    }
}
