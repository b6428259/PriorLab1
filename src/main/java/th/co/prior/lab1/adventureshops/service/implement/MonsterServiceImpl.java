package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.repository.MonsterRepository;
import th.co.prior.lab1.adventureshops.service.MonsterService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class MonsterServiceImpl implements MonsterService {

    private final MonsterRepository monsterRepository;
    private final PlayerServiceImpl playerService;
    private final InventoryServiceImpl inventoryService;
    @Override
    public ResponseModel<List<MonsterEntity>> getAllMonsters() {
        ResponseModel<List<MonsterEntity>> result = new ResponseModel<>();
        try {
            List<MonsterEntity> monsters = monsterRepository.findAll();
            if (!monsters.isEmpty()) {
                result.setStatus(200);
                result.setDescription("List of all monsters retrieved successfully.");
                result.setData(monsters);
            } else {
                result.setStatus(404);
                result.setDescription("No Monsters Found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterEntity> getMonsterById(Integer id) {
        ResponseModel<MonsterEntity> result = new ResponseModel<>();
        try {
            MonsterEntity monster = monsterRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Monster not found!"));
            result.setStatus(200);
            result.setDescription("Monster '" + monster.getName() + "' retrieved successfully.");
            result.setData(monster);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResponseModel<String> attackMonster(Integer playerId, String monsterName, Integer damage) {
        ResponseModel<String> result = new ResponseModel<>();
        try {
            PlayerEntity player = playerService.getPlayerById(playerId).getData();
            Optional<MonsterEntity> optionalMonster = monsterRepository.findMonsterByName(monsterName);

            if (optionalMonster.isPresent() && player != null) {
                MonsterEntity monster = optionalMonster.get();
                if (monster.getHealth() < damage) {
                    inventoryService.addInventory(monster.getItemDrop(), player.getId(), monster.getId());

                    result.setStatus(200);
                    result.setDescription("OK");
                    result.setData("Congratulations! You have defeated the boss. You have received a " + monster.getItemDrop());
                } else {
                    result.setStatus(200);
                    result.setDescription("OK");
                    result.setData("Oh no! You were defeated by " + monster.getName() + " because your damage was not sufficient.");
                }
            } else {
                result.setStatus(404);
                result.setDescription("Not Found!");
                result.setData("Player or monster not found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}