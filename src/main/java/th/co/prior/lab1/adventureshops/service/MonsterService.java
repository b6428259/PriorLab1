package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.MonsterModel;

import java.util.List;

public interface MonsterService {

    ApiResponse<List<MonsterModel>> getAllMonster();

    ApiResponse<MonsterModel> getMonsterById(Integer id);

    ApiResponse<MonsterModel> createMonster(String name, Integer maxHealth, String dropItem);


    ApiResponse<MonsterModel> updateMonster(Integer id, String name, Integer maxHealth, String dropItem);


    ApiResponse<MonsterModel> attackMonster(Integer playerId, Integer monsterId);
}