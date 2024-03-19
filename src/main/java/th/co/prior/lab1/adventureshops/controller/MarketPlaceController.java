package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.exception.PlayerNotFoundException;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.model.MarketPlaceModel;
import th.co.prior.lab1.adventureshops.service.MarketPlaceService;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class MarketPlaceController {

    private final MarketPlaceService marketPlaceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MarketPlaceModel>>> getMarketPlace(){
        ApiResponse<List<MarketPlaceModel>> response = this.marketPlaceService.getAllMarkerPlace();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MarketPlaceModel>> getMarketPlaceById(@PathVariable Integer id){
        ApiResponse<MarketPlaceModel> response = this.marketPlaceService.getMarketPlaceById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/buy")
    public ResponseEntity<ApiResponse<InventoryModel>> buyItem(@RequestBody Map<String, Object> request) {
        Integer playerId = (Integer) request.get("playerId");
        Integer itemId = (Integer) request.get("itemId");

        ApiResponse<InventoryModel> response;
        try {
            response = marketPlaceService.buyItem(playerId, itemId);
        } catch (Exception e) {
            response = new ApiResponse<>();
            response.setStatus(500);
            response.setMessage("Internal Server Error");
            response.setDescription(e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping("/sell")
    public ResponseEntity<ApiResponse<MarketPlaceModel>> sellItem(@RequestBody Map<String, Object> request) {
        Integer playerId = (Integer) request.get("playerId");
        Integer itemId = (Integer) request.get("itemId");
        Integer priceInt = (Integer) request.get("price");

        if (playerId == null || itemId == null || priceInt  == null) {
            ApiResponse<MarketPlaceModel> response = new ApiResponse<>();
            response.setStatus(400);
            response.setMessage("Bad Request");
            response.setDescription("playerId, itemId, or price is missing or invalid");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        double price = priceInt.doubleValue(); // Convert integer price to double

        ApiResponse<MarketPlaceModel> response = marketPlaceService.sellItem(playerId, itemId, price);
        return ResponseEntity.status(response.getStatus()).body(response);
    }



}