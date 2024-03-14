package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;

import java.util.List;

public interface MonsterService {

    ResponseModel<List<MonsterEntity>> getAllMonsters();

    ResponseModel<MonsterEntity> getMonsterById(Integer id);

    ResponseModel<String> attackMonster(Integer characterId, String monsterName, Integer damage);
}