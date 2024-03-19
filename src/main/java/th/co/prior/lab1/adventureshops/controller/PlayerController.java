package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.model.PlayerModel;
import th.co.prior.lab1.adventureshops.service.PlayerService;
import th.co.prior.lab1.adventureshops.service.implement.PlayerServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlayerModel>>> getPlayer(){
        ApiResponse<List<PlayerModel>> response = this.playerService.getAllPlayer();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerModel>> getPlayerById(@PathVariable Integer id){
        ApiResponse<PlayerModel> response = this.playerService.getPlayerById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse<PlayerModel>> createPlayer(
//            @RequestParam String name) {
//        ApiResponse<PlayerModel> response = playerService.createPlayer(name);
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PlayerModel>> createPlayer(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name"); // Extracting name from the request body
        ApiResponse<PlayerModel> response = playerService.createPlayer(name);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


//    @PostMapping("/buy")
//    public ResponseEntity<ApiResponse<InventoryModel>> buyItem(@RequestBody Map<String, Object> request) {
//        Integer playerId = (Integer) request.get("playerId");
//        Integer itemId = (Integer) request.get("itemId");
//
//        ApiResponse<InventoryModel> response;
//        try {
//            response = marketPlaceService.buyItem(playerId, itemId);
//        } catch (Exception e) {
//            response = new ApiResponse<>();
//            response.setStatus(500);
//            response.setMessage("Internal Server Error");
//            response.setDescription(e.getMessage());
//        }
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<PlayerModel>> updatePlayer(
            @PathVariable Integer id,
            @RequestParam String name
    ) {
        ApiResponse<PlayerModel> response = playerService.updatePlayer(id, name);
        return ResponseEntity.status(response.getStatus()).body(response);
    }}
