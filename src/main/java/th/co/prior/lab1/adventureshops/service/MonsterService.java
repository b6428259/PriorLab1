package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.List;

public interface MonsterService {

    ApiResponse<List<MonsterEntity>> getAllMonsters();

    ApiResponse<MonsterEntity> getMonsterById(Integer id);

    ApiResponse<String> attackMonster(Integer characterId, String monsterName, Integer damage);
}