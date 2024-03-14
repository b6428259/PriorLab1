package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.service.implement.PlayerServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    private final PlayerServiceImpl playerService;

    @GetMapping
    public ResponseEntity<ResponseModel<List<PlayerEntity>>> getAllPlayers() {
        ResponseModel<List<PlayerEntity>> response = playerService.getAllPlayers();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<PlayerEntity>> getPlayerById(@PathVariable Integer id) {
        ResponseModel<PlayerEntity> response = playerService.getPlayerById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
