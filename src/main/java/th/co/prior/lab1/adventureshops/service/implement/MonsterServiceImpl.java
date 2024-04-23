package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.EntityDto;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.repository.MonsterRepository;
import th.co.prior.lab1.adventureshops.service.LevelService;
import th.co.prior.lab1.adventureshops.service.MonsterService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MonsterServiceImpl implements MonsterService {

    private final EntityDto entityDTO;
    private final MonsterRepository monsterRepository;
    private final MonsterDto monsterDTO;
    private final PlayerDto playerDTO;
    private final InventoryDto inventoryDto;
    private final LevelService levelService;

    private static final Logger logger = LoggerFactory.getLogger(MonsterServiceImpl.class);


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
    public ApiResponse<MonsterModel> attackMonster(Integer playerId, Integer monsterId) {
        ApiResponse<MonsterModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Bad Request");

        try {
            PlayerEntity player = playerDTO.findPlayerById(playerId);
            MonsterEntity monster = monsterDTO.findMonsterById(monsterId);

            if (player != null && monster != null) {
                Integer levelId = player.getLevelId();
                if (levelId != null) {
                    LevelEntity levelEntity = levelService.findById(Long.valueOf(levelId));
                    int damage = levelEntity.getDamage();

                    if (monster.getHealth() <= damage) {
                        inventoryDto.addInventory(monster.getItemDrop(), player.getId(), monster.getId());

                        result.setStatus(HttpStatus.OK.value());
                        result.setMessage("OK");
                        result.setDescription("You have successfully killed the " + monster.getName() +". You have received a " + monster.getItemDrop());
                        result.setData(monsterDTO.toDTO(monster));
                    } else {
                        result.setStatus(HttpStatus.OK.value());
                        result.setMessage("OK");
                        result.setDescription("You have attacked " + monster.getName() + ". The monster's health is greater than your damage. Your damage is " + damage + ". Monster Health is " + monster.getHealth());
                        result.setData(monsterDTO.toDTO(monster));
                    }
                } else {
                    // Handle case where level ID is null
                    result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    result.setMessage("Internal Server Error");
                    result.setDescription("Player's level ID is null.");
                }
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setMessage("Not Found");
                result.setDescription("Player or monster not found.");
            }
        } catch (Exception e) {
            // Log the exception
            logger.error("An error occurred while attacking monster", e);

            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        // Log the result
        logger.info("Attack Monster Result: {}", result);

        return result;
    }



}