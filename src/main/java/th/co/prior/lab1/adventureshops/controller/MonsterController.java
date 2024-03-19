package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.entity.response.AttackResponse;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.MonsterServiceImpl;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/monster")
public class MonsterController {

    private final MonsterServiceImpl monsterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MonsterEntity>>> getAllMonsters() {
        ApiResponse<List<MonsterEntity>> response = monsterService.getAllMonsters();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MonsterEntity>> getMonsterById(@PathVariable Integer id) {
        ApiResponse<MonsterEntity> response = monsterService.getMonsterById(id);
        HttpStatus status = response.getStatus() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/attack")
    public ResponseEntity<ApiResponse<String>> attackBoss(@RequestBody AttackResponse request) {
        ApiResponse<String> response = monsterService.attackMonster(request.getPlayerId(), request.getMonsterName(), request.getDamage());
        HttpStatus status = response.getStatus() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}
