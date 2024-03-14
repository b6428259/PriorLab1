package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.service.InventoryService;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ResponseModel<List<InventoryEntity>>> getAllInventory() {
        ResponseModel<List<InventoryEntity>> response = inventoryService.getAllInventories();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<InventoryEntity>> getInventoryById(@PathVariable Integer id) {
        ResponseModel<InventoryEntity> response = inventoryService.getInventoryById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
