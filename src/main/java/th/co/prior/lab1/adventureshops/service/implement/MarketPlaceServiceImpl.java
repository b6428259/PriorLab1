package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.repository.MarketPlaceRepository;
import th.co.prior.lab1.adventureshops.service.MarketPlaceService;

import java.util.List;


@Service
@AllArgsConstructor
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketPlaceServiceImpl.class);
    private final MarketPlaceRepository marketPlaceRepository;
    private final PlayerServiceImpl playerService;
    private final InventoryServiceImpl inventoryService;
    private final InboxServiceImpl inboxService;
    private final AccountServiceImpl accountService;

    @Override
    public ApiResponse<List<MarketPlaceEntity>> getAllMarketPlaces() {
        ApiResponse<List<MarketPlaceEntity>> result = new ApiResponse<>();

        try {
            List<MarketPlaceEntity> marketPlaces = marketPlaceRepository.findAll();

            if (!marketPlaces.isEmpty()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(marketPlaces);
            } else {
                result.setStatus(404);
                result.setDescription("Not Found");
            }
        } catch (Exception e) {
            LOGGER.error("Error while fetching all market places: {}", e.getMessage());
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MarketPlaceEntity> getMarketPlaceById(Integer id) {
        ApiResponse<MarketPlaceEntity> result = new ApiResponse<>();

        try {
            MarketPlaceEntity marketPlace = marketPlaceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Market Place not found!"));

            result.setStatus(200);
            result.setDescription("OK");
            result.setData(marketPlace);
        } catch (Exception e) {
            LOGGER.error("Error while fetching market place by id {}: {}", id, e.getMessage());
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public void addMarketPlace(Integer characterId, Integer inventoryId, double price) {
        try {
            PlayerEntity character = playerService.getPlayerById(characterId).getData();
            InventoryEntity inventory = inventoryService.getInventoryById(inventoryId).getData();

            if (character != null && inventory != null) {
                MarketPlaceEntity marketPlace = new MarketPlaceEntity();
                marketPlace.setPlayer(character);
                marketPlace.setInventory(inventory);
                marketPlace.setCost(price);
                marketPlace.setSold(false);
                marketPlaceRepository.save(marketPlace);
            }
        } catch (Exception e) {
            LOGGER.error("Error while adding market place: {}", e.getMessage());
        }
    }

    @Override
    public ApiResponse<String> sellItem(Integer characterId, Integer itemId, double price) {
        ApiResponse<String> result = new ApiResponse<>();

        try {
            PlayerEntity character = playerService.getPlayerById(characterId).getData();
            InventoryEntity inventory = inventoryService.getInventoryById(itemId).getData();

            if (character != null && inventory != null && inventory.getPlayer().equals(character)) {
                List<MarketPlaceEntity> marketPlaces = marketPlaceRepository.findAll();

                if (checkItemIdIsNotEquals(marketPlaces, inventory)) {
                    addMarketPlace(characterId, itemId, price);

                    inboxService.addInbox(
                            characterId,
                            "Your " + inventory.getName() + " has been added to the market.");

                    result.setStatus(200);
                    result.setDescription("OK");
                    result.setData("Item successfully added to the market.");
                } else {
                    result.setStatus(400);
                    result.setDescription("Bad Request");
                    result.setData("Item is already listed on the market.");
                }
            } else {
                result.setStatus(404);
                result.setDescription("Not Found");
                result.setData("Character or item not found.");
            }
        } catch (Exception e) {
            LOGGER.error("Error while selling item: {}", e.getMessage());
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<String> buyItem(Integer characterId, Integer itemId, double price) {
        ApiResponse<String> result = new ApiResponse<>();

        try {
            MarketPlaceEntity marketPlace = getMarketPlaceById(itemId).getData();
            PlayerEntity character = playerService.getPlayerById(characterId).getData();

            if (marketPlace != null && character != null) {
                if (!marketPlace.isSold()) {
                    if (marketPlace.getCost() <= price) {
                        accountService.depositBalance(marketPlace.getPlayer().getId(), price);
                        accountService.withdrawBalance(characterId, price);

                        inventoryService.changeOwner(character, marketPlace.getInventory());

                        marketPlace.setSold(true);
                        marketPlaceRepository.save(marketPlace);

                        inboxService.addInbox(
                                marketPlace.getPlayer().getId(),
                                "Your " + marketPlace.getInventory().getName() + " has been sold.");

                        result.setStatus(200);
                        result.setDescription("OK");
                        result.setData("Successfully purchased a " + marketPlace.getInventory().getName() + ".");
                    } else {
                        result.setStatus(400);
                        result.setDescription("Bad Request");
                        result.setData("Insufficient balance to purchase " + marketPlace.getInventory().getName());
                    }
                } else {
                    result.setStatus(400);
                    result.setDescription("Bad Request");
                    result.setData("The " + marketPlace.getInventory().getName() + " has already been sold.");
                }
            } else {
                result.setStatus(404);
                result.setDescription("Not Found");
                result.setData("Market place or character not found.");
            }

        } catch (Exception e) {
            LOGGER.error("Error while buying item: {}", e.getMessage());
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    private boolean checkItemIdIsNotEquals(List<MarketPlaceEntity> market, InventoryEntity inventory) {
        return market.stream().noneMatch(e -> e.getInventory().getId().equals(inventory.getId()));
    }
}
