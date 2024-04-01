package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.service.InventoryService;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryEntity>>> getAllInventory() {
        ApiResponse<List<InventoryEntity>> response = inventoryService.getAllInventories();
        return ResponseEntity.status(response.getStatus()).body(response);
    }


//    @GetMapping("/name/{name}")
//    public ApiResponse<InventoryModel> getInventoryByName(@PathVariable("name") String name) {
//        return inventoryService.getInventoryByName(name);
//    }

//    @GetMapping("/name/{name}")
//    public ResponseEntity<ApiResponse<InventoryModel>> getInventoryByName(@PathVariable("name") String name) {
//        ApiResponse<InventoryModel> response = inventoryService.getInventoryByName(name);
//
//        return  ResponseEntity.status(response.getStatus()).body(response);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryModel>> getInventoryById(@PathVariable Integer id) {
        ApiResponse<InventoryModel> response = inventoryService.getInventoryById(id);
        if (response.getStatus() == 200) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == 404) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

}
