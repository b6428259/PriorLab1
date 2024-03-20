package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.response.AttackResponse;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.service.MonsterService;
import th.co.prior.lab1.adventureshops.service.implement.MonsterServiceImpl;


import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/monster")
public class MonsterController {

    private final MonsterService monsterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MonsterModel>>> getAllMonsters() {
        ApiResponse<List<MonsterModel>> response = monsterService.getAllMonster();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MonsterModel>> getMonsterById(@PathVariable Integer id) {
        ApiResponse<MonsterModel> response = monsterService.getMonsterById(id);
        HttpStatus status = response.getStatus() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MonsterModel>> createMonster(
            @RequestParam String name,
            @RequestParam Integer health,
            @RequestParam String dropItem
    ) {
        ApiResponse<MonsterModel> response = monsterService.createMonster(name, health, dropItem);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping("/attack")
    public ResponseEntity<ApiResponse<MonsterModel>> attackMonster(@RequestBody Map<String, Integer> requestBody) {
        Integer playerId = requestBody.get("playerId");
        Integer monsterId = requestBody.get("monsterId");
    {
        ApiResponse<MonsterModel> response = this.monsterService.attackMonster(playerId , monsterId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}}

