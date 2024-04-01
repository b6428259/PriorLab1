package th.co.prior.lab1.adventureshops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.repository.InventoryRepository;
import th.co.prior.lab1.adventureshops.service.implement.InventoryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

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
    public void testGetAllInventories() {
        // Prepare data
        List<InventoryEntity> inventories = new ArrayList<>();
        inventories.add(new InventoryEntity());
        when(inventoryRepository.findAll()).thenReturn(inventories);

        // Call the method
        ApiResponse<List<InventoryEntity>> response = inventoryService.getAllInventories();

        // Verify the result
        assertEquals(200, response.getStatus());
        assertEquals("List of all Inventories retrieved successfully.", response.getDescription());
        assertEquals(inventories, response.getData());
    }

    @Test
    public void testGetInventoryById() {
        // Prepare data
        InventoryEntity inventory = new InventoryEntity();
        when(inventoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(inventory));

        // Call the method
        ApiResponse<InventoryModel> response = inventoryService.getInventoryById(1);

        // Verify the result
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getDescription());
        // Assuming toDTO method returns non-null value
        verify(inventoryDto, times(1)).toDTO(any(InventoryEntity.class));
    }

}
