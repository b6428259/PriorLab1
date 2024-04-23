package th.co.prior.lab1.adventureshops.service.inventory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.InventoryServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetInventoryTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private PlayerDto playerDto;

    @Mock
    private MonsterDto monsterDto;

    @Mock
    private InventoryDto inventoryDto;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void testGetAllInventories() {
        // Arrange
        InventoryEntity inventory1 = new InventoryEntity();
        InventoryEntity inventory2 = new InventoryEntity();
        List<InventoryEntity> inventories = Arrays.asList(inventory1, inventory2);

        when(inventoryRepository.findAll()).thenReturn(inventories);

        // Act
        ApiResponse<List<InventoryEntity>> response = inventoryService.getAllInventories();

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("List of all Inventories retrieved successfully.", response.getDescription());
        assertEquals(inventories, response.getData());
    }

    @Test
    void testGetAllInventories_EmptyList() {
        // Arrange
        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ApiResponse<List<InventoryEntity>> response = inventoryService.getAllInventories();

        // Assert
        assertEquals(404, response.getStatus());
        assertEquals("No Inventories Found!", response.getDescription());
        assertEquals(Collections.emptyList(), response.getData());
    }

    @Test
    void testGetInventoryById() {
        // Arrange
        InventoryEntity inventory = new InventoryEntity();
        inventory.setId(1);
        Optional<InventoryEntity> optionalInventory = Optional.of(inventory);

        when(inventoryRepository.findById(1)).thenReturn(optionalInventory);
        when(inventoryDto.toDTO(inventory)).thenReturn(new InventoryModel());

        // Act
        ApiResponse<InventoryModel> response = inventoryService.getInventoryById(1);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getDescription());
        assertEquals(new InventoryModel(), response.getData());
    }

    @Test
    void testGetAllInventories_InternalServerError() {
        // Arrange
        doThrow(new RuntimeException("Database connection failed")).when(inventoryRepository).findAll();

        // Act
        ApiResponse<List<InventoryEntity>> response = inventoryService.getAllInventories();

        // Assert
        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getDescription());
        assertTrue(response.getData().isEmpty());
    }

    @Test
    void testGetInventoryById_InternalServerError() {
        // Arrange
        int inventoryId = 1;
        doThrow(new RuntimeException("Database connection failed")).when(inventoryRepository).findById(anyInt());

        // Act
        ApiResponse<InventoryModel> response = inventoryService.getInventoryById(inventoryId);

        // Assert
        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getDescription());
        assertNull(response.getData());
    }
}
