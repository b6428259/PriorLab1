package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.entity.response.ItemResponse;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.MarketPlaceServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class MarketPlaceController {

    private final MarketPlaceServiceImpl marketPlaceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MarketPlaceEntity>>> getAllMarketPlace() {
        ApiResponse<List<MarketPlaceEntity>> response = marketPlaceService.getAllMarketPlaces();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/sell")
    public ResponseEntity<ApiResponse<String>> sellItem(@RequestBody ItemResponse request) {
        ApiResponse<String> response = marketPlaceService.sellItem(request.getPlayerId(), request.getItemId(), request.getCost());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/buy")
    public ResponseEntity<ApiResponse<String>> buyItem(@RequestBody ItemResponse request) {
        ApiResponse<String> response = marketPlaceService.buyItem(request.getPlayerId(), request.getItemId(), request.getCost());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
