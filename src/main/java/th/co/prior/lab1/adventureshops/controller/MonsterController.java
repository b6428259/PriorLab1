package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.entity.response.AttackResponse;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.service.implement.MonsterServiceImpl;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/monster")
public class MonsterController {

    private final MonsterServiceImpl monsterService;

    @GetMapping
    public ResponseEntity<ResponseModel<List<MonsterEntity>>> getAllMonsters() {
        ResponseModel<List<MonsterEntity>> response = monsterService.getAllMonsters();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<MonsterEntity>> getMonsterById(@PathVariable Integer id) {
        ResponseModel<MonsterEntity> response = monsterService.getMonsterById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/attack")
    public ResponseEntity<ResponseModel<String>> attackBoss(@RequestBody AttackResponse request) {
        ResponseModel<String> response = monsterService.attackMonster(request.getPlayerId(), request.getMonsterName(), request.getDamage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
