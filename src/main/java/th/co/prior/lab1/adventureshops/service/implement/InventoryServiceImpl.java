package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;
import th.co.prior.lab1.adventureshops.service.InventoryService;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class InventoryServiceImpl implements InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepository inventoryRepository;
    private final PlayerServiceImpl playerService;
    private final MonsterServiceImpl monsterService;

    @Override
    public ResponseModel<List<InventoryEntity>> getAllInventories() {
        ResponseModel<List<InventoryEntity>> result = new ResponseModel<>();
        try {
            List<InventoryEntity> inventories = this.inventoryRepository.findAll();
            if (!inventories.isEmpty()) {
                result.setStatus(200);
                result.setDescription("List of all Inventories retrieved successfully.");
                result.setData(inventories);
            } else {
                result.setStatus(404);
                result.setDescription("Not Inventories Found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResponseModel<InventoryEntity> getInventoryById(Integer id) {
        ResponseModel<InventoryEntity> result = new ResponseModel<>();
        try {
            InventoryEntity inventory = this.inventoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Inventory not found!"));
            result.setStatus(200);
            result.setDescription("OK");
            result.setData(inventory);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public void addInventory(String name, Integer characterId, Integer monsterId) {
        ResponseModel<String> result = new ResponseModel<>();
        try {
            ResponseModel<PlayerEntity> characterResponse = playerService.getPlayerById(characterId);
            ResponseModel<MonsterEntity> monsterResponse = monsterService.getMonsterById(monsterId);

            PlayerEntity character = characterResponse.getData();
            MonsterEntity monster = monsterResponse.getData();

            if (character != null && monster != null) {
                InventoryEntity inventory = new InventoryEntity();
                inventory.setName(name);
                inventory.setPlayer(character);
                inventory.setMonster(monster);
                inventoryRepository.save(inventory);

                result.setStatus(200);
                result.setDescription("OK");
                result.setData("You have successfully added inventory.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
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