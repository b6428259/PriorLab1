package th.co.prior.lab1.adventureshops.service.market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import th.co.prior.lab1.adventureshops.component.kafka.component.KafkaProducerComponent;
import th.co.prior.lab1.adventureshops.dto.InboxDto;
import th.co.prior.lab1.adventureshops.dto.InventoryDto;
import th.co.prior.lab1.adventureshops.dto.MarketDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.MarketPlaceModel;
import th.co.prior.lab1.adventureshops.service.implement.MarketPlaceServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class SellItemTest {

    @Mock
    private PlayerDto playerDTO;

    @Mock
    private InventoryDto inventoryDTO;

    @Mock
    private MarketDto marketDTO;

    @Mock
    private InboxDto inboxDTO;

    @Mock
    private KafkaProducerComponent kafkaProducerComponent;

    @InjectMocks
    private MarketPlaceServiceImpl marketplaceDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSellItem_Success() {
        // Arrange
        Integer playerId = 1;
        Integer itemId = 1;
        double price = 10.0;

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(playerId);

        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setId(itemId);

        MarketPlaceEntity marketPlaceEntity = new MarketPlaceEntity();

        when(playerDTO.findPlayerById(playerId)).thenReturn(playerEntity);
        when(inventoryDTO.findInventoryById(itemId)).thenReturn(inventoryEntity);
        when(marketDTO.hasOwner(any(), any())).thenReturn(true);
        when(marketDTO.checkItemIdIsNotEquals(any(), any())).thenReturn(true);
        when(marketDTO.toDTO(any())).thenReturn(new MarketPlaceModel()); // Changed to MarketPlaceModel

        // Act
        ApiResponse<MarketPlaceModel> response = marketplaceDto.sellItem(playerId, itemId, price);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("Created", response.getMessage());
        assertNotNull(response.getData());

        verify(marketDTO, times(1)).addMarketPlace(playerId, itemId, price);
        verify(inboxDTO, times(1)).addInbox(playerId, "Your " + inventoryEntity.getName() + " has been added to the market.");
        verify(kafkaProducerComponent, times(1)).send("report-message", null, null, "Your " + inventoryEntity.getName() + " has been added to the market.");
    }

    // Add more test cases for other scenarios (e.g., player not found, inventory not found, etc.)
}
