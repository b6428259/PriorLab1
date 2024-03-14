package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.repository.MarketPlaceRepository;
import th.co.prior.lab1.adventureshops.service.MarketPlaceService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseModel<List<MarketPlaceEntity>> getAllMarketPlaces() {
        ResponseModel<List<MarketPlaceEntity>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if(marketPlaces.iterator().hasNext()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(marketPlaces);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceEntity> getMarketPlaceById(Integer id) {
        ResponseModel<MarketPlaceEntity> result = new ResponseModel<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            MarketPlaceEntity marketPlaces = this.marketPlaceRepository.findById(id).orElseThrow(() -> new RuntimeException("Market Place not found!"));

            result.setStatus(200);
            result.setDescription("OK");
            result.setData(marketPlaces);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public void addMarketPlace(Integer characterId, Integer inventoryId, double price) {
        try {
            PlayerEntity character = this.playerService.getPlayerById(characterId).getData();
            InventoryEntity inventory = this.inventoryService.getInventoryById(inventoryId).getData();

            if(Objects.nonNull(character) && Objects.nonNull(inventory)) {
                MarketPlaceEntity marketPlace = new MarketPlaceEntity();
                marketPlace.setPlayer(character);
                marketPlace.setInventory(inventory);
                marketPlace.setCost(price);
                marketPlace.setSold(false);
                this.marketPlaceRepository.save(marketPlace);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    @Override
    public ResponseModel<String> sellItem(Integer characterId, Integer itemId, double price) {
        ResponseModel<String> result = new ResponseModel<>();
        result.setStatus(404);
        result.setDescription("Not Found!");
        result.setData("Item not found in Inventory.");

        try {
            PlayerEntity character = this.playerService.getPlayerById(characterId).getData();
            InventoryEntity inventory = this.inventoryService.getInventoryById(itemId).getData();
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if(Objects.nonNull(character) && Objects.nonNull(inventory) && inventory.getPlayer().equals(character)) {
                if(this.checkItemIdIsNotEquals(marketPlaces, inventory)) {
                        this.addMarketPlace(character.getId(), inventory.getId(), price);

                        this.inboxService.addInbox(
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
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<String> buyItem(Integer characterId, Integer itemId, double price) {
        ResponseModel<String> result = new ResponseModel<>();
        result.setStatus(404);
        result.setDescription("Not Found!");
        result.setData("There are no products for sale on the market.");

        try {
            MarketPlaceEntity marketPlaces = this.getMarketPlaceById(itemId).getData();
            PlayerEntity character = this.playerService.getPlayerById(characterId).getData();
            InventoryEntity inventory = this.inventoryService.getInventoryById(itemId).getData();

            if(Objects.nonNull(character) && Objects.nonNull(inventory) && Objects.nonNull(marketPlaces)) {
                if (!marketPlaces.isSold()) {
                    if (marketPlaces.getCost() <= price) {
                        this.accountService.depositBalance(marketPlaces.getPlayer().getId(), price);
                        this.accountService.withdrawBalance(character.getId(), price);

                        this.inventoryService.changeOwner(character, inventory);

                        marketPlaces.setSold(true);
                        this.marketPlaceRepository.save(marketPlaces);

                        this.inboxService.addInbox(
                                marketPlaces.getPlayer().getId(),
                                "Your " + marketPlaces.getInventory().getName() + " has been sold.");

                        result.setStatus(200);
                        result.setDescription("OK");
                        result.setData("Successfully purchased a " + marketPlaces.getInventory().getName() + ".");
                    } else {
                        result.setStatus(400);
                        result.setDescription("Bad Request");
                        result.setData("Insufficient balance to purchase " + marketPlaces.getInventory().getName());
                    }
                } else {
                    result.setStatus(400);
                    result.setDescription("Bad Request");
                    result.setData("The " + marketPlaces.getInventory().getName() + " has already been sold.");
                }
            } else {
                    result.setStatus(404);
                    result.setDescription("Not Found");
                    result.setData("Item not found on the market.");
                }

        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    private boolean checkItemIdIsNotEquals(List<MarketPlaceEntity> market, InventoryEntity inventory) {
        return Optional.ofNullable(market)
                .map(m -> m.isEmpty() || m.stream().anyMatch(e -> !e.getInventory().getId().equals(inventory.getId())))
                .orElse(false);
    }
}
