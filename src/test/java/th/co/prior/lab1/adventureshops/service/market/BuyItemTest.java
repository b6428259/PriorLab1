package th.co.prior.lab1.adventureshops.service.market;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import th.co.prior.lab1.adventureshops.component.kafka.component.KafkaProducerComponent;
import th.co.prior.lab1.adventureshops.dto.*;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.repository.MarketPlaceRepository;
import th.co.prior.lab1.adventureshops.service.implement.MarketPlaceServiceImpl;

public class BuyItemTest {

    @Mock
    private MarketPlaceRepository marketPlaceRepository;

    @Mock
    private MarketDto marketDto;

    @Mock
    private PlayerDto playerDTO;

    @Mock
    private InventoryDto inventoryDto;

    @Mock
    private InboxDto inboxDto;

    @Mock
    private AccountDto accountDto;


    @Mock
    private EntityDto entityDto;

    @Mock
    private KafkaProducerComponent kafkaProducerComponent;

    @InjectMocks
    private MarketPlaceServiceImpl marketPlaceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuyItem_InventoryItemNotFound() {
        // Mocking necessary objects and their behaviors
        MarketPlaceEntity marketPlaceEntity = new MarketPlaceEntity();
        marketPlaceEntity.setId(1);
        marketPlaceEntity.setCost(50);
        marketPlaceEntity.setSold(false);

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(1); // Adjusted to match player ID used in the test

        // Mocking null inventory entity
        marketPlaceEntity.setInventory(null);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1);
        accountEntity.setBalance(100);

        // Mocking behavior for when inventory entity is null
        when(marketDto.findMarketPlaceById(1)).thenReturn(marketPlaceEntity); // Adjusted to match market ID used in the test
        when(playerDTO.findPlayerById(1)).thenReturn(playerEntity);
        when(inventoryDto.findInventoryById(null)).thenReturn(null); // Adjusted to match null inventory ID
        when(accountDto.findAccountById(1)).thenReturn(accountEntity);
        when(entityDto.hasEntity(playerEntity, null)).thenReturn(true); // Mocking null inventory entity

        // Calling the method under test
        ApiResponse<InventoryModel> response = marketPlaceService.buyItem(1, 1); // Adjusted to match market ID used in the test

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus(), "Unexpected status code");
        assertEquals("Bad Request", response.getMessage(), "Unexpected message");
        assertEquals("Inventory item is null for market item with ID: 1", response.getDescription(), "Unexpected description");
        // Add more assertions as per your requirements
    }
    @Test
    void testBuyItem_SuccessfulPurchase() {
        // Arrange
        int playerId = 1;
        int itemId = 1;

        MarketPlaceEntity marketPlaceEntity = new MarketPlaceEntity();
        marketPlaceEntity.setId(itemId);
        marketPlaceEntity.setCost(50.0);
        marketPlaceEntity.setSold(false);

        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setId(itemId);
        inventoryEntity.setName("Test Item");
        marketPlaceEntity.setInventory(inventoryEntity);

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(playerId);

        PlayerEntity sellerPlayer = new PlayerEntity();
        sellerPlayer.setId(2); // Set the appropriate seller player ID
        marketPlaceEntity.setPlayer(sellerPlayer);


        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(playerId);
        accountEntity.setBalance(100.0);
        playerEntity.setAccount(accountEntity);

        when(marketDto.findMarketPlaceById(itemId)).thenReturn(marketPlaceEntity);
        when(playerDTO.findPlayerById(playerId)).thenReturn(playerEntity);
        when(inventoryDto.findInventoryById(itemId)).thenReturn(inventoryEntity);
        when(accountDto.findAccountById(playerId)).thenReturn(accountEntity);
        when(entityDto.hasEntity(playerEntity, inventoryEntity)).thenReturn(true);

        // Act
        ApiResponse<InventoryModel> response = marketPlaceService.buyItem(playerId, itemId);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals("Successfully purchased a Test Item.", response.getDescription());
    }

}

