package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.*;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.exception.*;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.model.MarketPlaceModel;
import th.co.prior.lab1.adventureshops.repository.MarketPlaceRepository;
import th.co.prior.lab1.adventureshops.service.MarketPlaceService;


import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketDto marketDto;
    private final PlayerDto playerDTO;
    private final InventoryDto inventoryDto;
    private final InboxDto inboxDto;
    private final AccountDto accountDto;
    private final EntityDto entityDto;

    @Override
    public ApiResponse<List<MarketPlaceModel>> getAllMarkerPlace() {
        ApiResponse<List<MarketPlaceModel>> result = new ApiResponse<>();
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setMessage("Not Found!");

        try {
            List<MarketPlaceEntity> marketPlaces = marketPlaceRepository.findAll();

            if (!marketPlaces.isEmpty()) {
                result.setStatus(HttpStatus.OK.value());
                result.setMessage("OK");
                result.setDescription("Successfully retrieved markets information.");
                result.setData(marketDto.toDTOList(marketPlaces));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            result.setDescription("Marketplace not found!");
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MarketPlaceModel> getMarketPlaceById(Integer id) {
        ApiResponse<MarketPlaceModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setMessage("Not Found!");

        try {
            MarketPlaceEntity marketPlaces = marketPlaceRepository.findById(id)
                    .orElseThrow(() -> new NullPointerException("Marketplace not found!"));

            result.setStatus(HttpStatus.OK.value());
            result.setMessage("OK");
            result.setDescription("Successfully retrieved market information.");
            result.setData(marketDto.toDTO(marketPlaces));
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

        @Override
        public ApiResponse<InventoryModel> buyItem(Integer playerId, Integer itemId) {
            ApiResponse<InventoryModel> result = new ApiResponse<>();

            try {
                MarketPlaceEntity marketPlaces = marketDto.findMarketPlaceById(itemId);
                if (marketPlaces == null) {
                    throw new MarketItemNotFoundException("Market item not found with ID: " + itemId);
                }

                PlayerEntity player = playerDTO.findPlayerById(playerId);
                if (player == null) {
                    throw new PlayerNotFoundException("Player not found with ID: " + playerId);
                }

                InventoryEntity inventory = inventoryDto.findInventoryById(marketPlaces.getInventory().getId());
                if (inventory == null) {
                    throw new InventoryItemNotFoundException("Inventory item not found with ID: " + marketPlaces.getInventory().getId());
                }

                AccountEntity account = accountDto.findAccountById(player.getAccount().getId());
                if (account == null) {
                    throw new AccountNotFoundException("Account not found for player with ID: " + playerId);
                }

                if (this.entityDto.hasEntity(player, inventory)) {
                    if (!marketPlaces.isSold()) {
                        double cost = marketPlaces.getCost();
                        if (cost <= account.getBalance()) {
                            accountDto.depositBalance(marketPlaces.getPlayer().getId(), cost);
                            accountDto.withdrawBalance(playerId, cost);

                            inventoryDto.changeOwner(player, inventory);
                            inventoryDto.setOnMarket(inventory, false);

                            marketPlaces.setSold(true);
                            marketPlaceRepository.save(marketPlaces);

                            inboxDto.addInbox(marketPlaces.getPlayer().getId(),
                                    "Your " + marketPlaces.getInventory().getName() + " has been sold.");

                            result.setStatus(HttpStatus.OK.value());
                            result.setMessage("OK");
                            result.setDescription("Successfully purchased a " + marketPlaces.getInventory().getName() + ".");
                            result.setData(inventoryDto.toDTO(inventory));
                        } else {
                            throw new InsufficientFundsException("Your balance is insufficient.");
                        }
                    } else {
                        throw new ItemAlreadySoldException("The " + marketPlaces.getInventory().getName() + " has already been sold.");
                    }
                } else {
                    throw new UnauthorizedAccessException("You are not authorized to purchase this item.");
                }
            } catch (MarketItemNotFoundException | PlayerNotFoundException | InventoryItemNotFoundException | AccountNotFoundException | InsufficientFundsException | ItemAlreadySoldException | UnauthorizedAccessException e) {
                result.setStatus(HttpStatus.BAD_REQUEST.value());
                result.setMessage("Bad Request");
                result.setDescription(e.getMessage());
            } catch (Exception e) {
                result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                result.setMessage("Internal Server Error");
                result.setDescription(e.getMessage());
            }

            return result;
        }

    @Override
    public ApiResponse<MarketPlaceModel> sellItem(Integer playerId, Integer itemId, double price) {
        ApiResponse<MarketPlaceModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Bad Request");

        try {
            PlayerEntity player = playerDTO.findPlayerById(playerId);
            if (player == null) {
                throw new PlayerNotFoundException("Player not found with ID: " + playerId);
            }

            InventoryEntity inventory = inventoryDto.findInventoryById(itemId);
            if (inventory == null) {
                throw new InventoryItemNotFoundException("Inventory item not found with ID: " + itemId);
            }

            MarketPlaceEntity marketPlaces = marketDto.findMarketPlaceByInventoryId(inventory.getId());
            if (marketDto.hasOwner(player, inventory)) {
                if (marketDto.checkItemIdIsNotEquals(marketPlaces, inventory)) {
                    marketDto.addMarketPlace(playerId, inventory.getId(), price);
                    inboxDto.addInbox(playerId, "Your " + inventory.getName() + " has been added to the market.");

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setMessage("Created");
                    result.setDescription("You have successfully added a " + inventory.getName() + " to market.");
                    result.setData(marketDto.toDTO(marketDto.findMarketPlaceByInventoryId(inventory.getId())));
                } else {
                    result.setStatus(HttpStatus.BAD_REQUEST.value());
                    result.setMessage("Bad Request");
                    result.setDescription("You already have " + inventory.getName() + " on the market.");
                }
            } else {
                throw new UnauthorizedAccessException("Player is not the owner of the inventory item.");
            }
        } catch (PlayerNotFoundException | InventoryItemNotFoundException | UnauthorizedAccessException e) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMessage("Bad Request");
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MarketPlaceModel> deleteMarketPlace(Integer id) {
        ApiResponse<MarketPlaceModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Bad Request");

        try {
            marketPlaceRepository.findById(id).orElseThrow(() -> new NullPointerException("Marketplace not found!"));
            marketPlaceRepository.deleteById(id);

            result.setStatus(HttpStatus.OK.value());
            result.setMessage("OK");
            result.setDescription("Market data has been deleted.");
            result.setData(null);
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
