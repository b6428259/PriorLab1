package th.co.prior.lab1.adventureshops.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.LevelService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/levels")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @GetMapping()
    public ResponseEntity<?> getLevel(){
        List<LevelEntity> level = levelService.retrieveLevel();
        return ResponseEntity.ok(level);
    }

    @PostMapping("/create")
    public ResponseEntity<LevelEntity> postLevel(@RequestBody LevelEntity body) {
        LevelEntity level = levelService.createLevel(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(level);
    }
}
