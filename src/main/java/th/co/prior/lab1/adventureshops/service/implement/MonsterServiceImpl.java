package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.EntityDTO;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MonsterDTO;
import th.co.prior.lab1.adventureshops.dto.PlayerDTO;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.repository.MonsterRepository;
import th.co.prior.lab1.adventureshops.service.LevelService;
import th.co.prior.lab1.adventureshops.service.MonsterService;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MonsterServiceImpl implements MonsterService {

    private final EntityDTO entityDTO;
    private final MonsterRepository monsterRepository;
    private final MonsterDTO monsterDTO;
    private final PlayerDTO playerDTO;
    private final InventoryDto inventoryDto;
    private final LevelService levelService;


    @Override
    public ApiResponse<List<MonsterModel>> getAllMonster() {
        ApiResponse<List<MonsterModel>> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<MonsterEntity> monsters = this.monsterRepository.findAll();

            if(monsters.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved monsters information.");
                result.setData(this.monsterDTO.toDTOList(monsters));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Monster not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MonsterModel> getMonsterById(Integer id) {
        ApiResponse<MonsterModel> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            MonsterEntity monsters = this.monsterRepository.findById(id).orElseThrow(() -> new NullPointerException("Monster not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved monster information.");
            result.setData(this.monsterDTO.toDTO(monsters));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MonsterModel> createMonster(String name, Integer maxHealth, String itemDrop) {
        ApiResponse<MonsterModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            Optional<MonsterEntity> duplicateMonsterName = this.monsterRepository.findMonsterByName(name);
            Optional<MonsterEntity> duplicateMonsterDropItem = this.monsterRepository.findMonsterByItemDrop(itemDrop);

            if(duplicateMonsterName.isEmpty() && duplicateMonsterDropItem.isEmpty()) {
                MonsterEntity monster = new MonsterEntity();
                monster.setName(name);
                monster.setHealth(maxHealth);
                monster.setItemDrop(itemDrop);
                MonsterEntity saved = this.monsterRepository.save(monster);

                result.setStatus(201);
                result.setMessage("Created");
                result.setDescription("Successfully created monster information.");
                result.setData(this.monsterDTO.toDTO(saved));
            } else {
                result.setDescription("You already have an monster or drop item.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MonsterModel> updateMonster(Integer id, String name, Integer health, String dropItem) {
        ApiResponse<MonsterModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            MonsterEntity monster = this.monsterRepository.findById(id).orElseThrow(() -> new NullPointerException("Monster noy found!"));

            monster.setName(name);
            monster.setHealth(health);
            monster.setItemDrop(dropItem);
            this.monsterRepository.save(monster);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Monster information has been successfully updated.");
            result.setData(this.monsterDTO.toDTO(monster));

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
    public ApiResponse<MonsterModel> attackMonster(Integer playerId, Integer monsterId) {
        ApiResponse<MonsterModel> result = new ApiResponse<>();
        LevelEntity levelEntity = levelService.findById(Long.valueOf(playerDTO.findPlayerById(playerId).getLevelId()));
        Integer damage = levelEntity.getDamage();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            // Retrieve player and monster entities from the database

            PlayerEntity player = this.playerDTO.findPlayerById(playerId);
            MonsterEntity monster = this.monsterDTO.findMonsterById(monsterId);




                    // Proceed with logic if player's level damage is greater than or equal to monster's health
                    if (monster.getHealth().intValue() <= damage.intValue()) {
                        this.inventoryDto.addInventory(monster.getItemDrop(), player.getId(), monster.getId());

                        result.setStatus(200);
                        result.setMessage("OK");
                        result.setDescription("You have successfully killed the boss. You have received a " + monster.getItemDrop());
                        result.setData(this.monsterDTO.toDTO(monster));
                    } else {
                        result.setStatus(200);
                        result.setMessage("OK");
                        result.setDescription("You have been killed by " + monster.getName() + ". Because the damage is not enough"
                                +" Your damage is" + damage +" Monster Health is " + monster.getHealth().intValue());
                        result.setData(this.monsterDTO.toDTO(monster));

                    }

//            } else {
//                String playerName = (playerId != null) ? "Player: " + playerDTO.findPlayerById(playerId).getName() : "Unknown player";
//                String monsterName = (monsterId != null) ? "Monster: " + monsterDTO.findMonsterById(monsterId).getName() : "Unknown monster";
//                // Handle case where player or monster entity is null
//                result.setDescription("Can't found Character or Monster!" + playerName + ", " + monsterName);
//            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());

        }

        return result;
    }
}