package th.co.prior.lab1.adventureshops.dto;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InventoryDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDto.class);
    private final EntityDto entityDTO;
    private final MonsterDto monsterDTO;
    private final PlayerDto playerDTO;
    private final InventoryRepository inventoryRepository;

    public List<InventoryModel> toDTOList(List<InventoryEntity> inventory) {
        return inventory.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public InventoryModel toDTO(InventoryEntity inventory){
        InventoryModel dto = new InventoryModel();
        dto.setId(inventory.getId());
        dto.setItem(inventory.getName());
        dto.setOwner(inventory.getPlayer().getName());
        dto.setDroppedBy(inventory.getMonster().getName());
        dto.setMarketable(inventory.isOnMarket());

        return dto;
    }



    public List<InventoryEntity> findAllInventory(){
        return inventoryRepository.findAll();
    }

        public InventoryEntity findInventoryById(Integer id){
            return inventoryRepository.findById(id).orElse(null);
        }


    public void addInventory(String name, Integer playerId, Integer monsterId) {
        try {
            PlayerEntity player = this.playerDTO.findPlayerById(playerId);
            MonsterEntity monster = this.monsterDTO.findMonsterById(monsterId);

            if (this.entityDTO.hasEntity(player, monster)) {
                InventoryEntity inventory = new InventoryEntity();
                inventory.setName(name);
                inventory.setPlayer(player);
                inventory.setMonster(monster);
                inventoryRepository.save(inventory);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    public void changeOwner(PlayerEntity character, InventoryEntity inventory){
        try {
            inventory.setPlayer(character);
            this.inventoryRepository.save(inventory);

        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    public void setOnMarket(InventoryEntity inventory, boolean value){
        try{
            inventory.setOnMarket(value);
            this.inventoryRepository.save(inventory);
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }
}