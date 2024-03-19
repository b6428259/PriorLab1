package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
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

    private final EntityDTO entityDTO;
    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketDto marketDto;
    private final PlayerDTO playerDTO;
    private final InventoryDto inventoryDto;
    private final InboxDto inboxDto;
    private final AccountDto accountDto;

    @Override
    public ApiResponse<List<MarketPlaceModel>> getAllMarkerPlace() {
        ApiResponse<List<MarketPlaceModel>> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found!");

        try {
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if (marketPlaces.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved markets information.");
                result.setData(this.marketDto.toDTOList(marketPlaces));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Marketplace not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MarketPlaceModel> getMarketPlaceById(Integer id) {
        ApiResponse<MarketPlaceModel> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found!");

        try {
            MarketPlaceEntity marketPlaces = this.marketPlaceRepository.findById(id).orElseThrow(() -> new NullPointerException("Marketplace not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved market information.");
            result.setData(this.marketDto.toDTO(marketPlaces));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<InventoryModel> buyItem(Integer playerId, Integer itemId) {
        ApiResponse<InventoryModel> result = new ApiResponse<>();


        try {
            MarketPlaceEntity marketPlaces = this.marketDto.findMarketPlaceById(itemId);
            if (marketPlaces == null) {
                throw new MarketItemNotFoundException("Market item not found with ID: " + itemId);
            }

            PlayerEntity player = this.playerDTO.findPlayerById(playerId);
            if (player == null) {
                throw new PlayerNotFoundException("Player not found with ID: " + playerId);
            }

            InventoryEntity inventory = this.inventoryDto.findInventoryById(marketPlaces.getInventory().getId());
            if (inventory == null) {
                throw new InventoryItemNotFoundException("Inventory item not found with ID: " + marketPlaces.getInventory().getId());
            }

            AccountEntity account = this.accountDto.findAccountById(player.getAccount().getId());
            if (account == null) {
                throw new AccountNotFoundException("Account not found for player with ID: " + playerId);
            }

            if (this.entityDTO.hasEntity(player, inventory)) {
                if (!marketPlaces.isSold()) {
                    if (marketPlaces.getCost() <= account.getBalance()) {
                        this.accountDto.depositBalance(marketPlaces.getPlayer().getId(), marketPlaces.getCost());
                        this.accountDto.withdrawBalance(player.getId(), marketPlaces.getCost());

                        this.inventoryDto.changeOwner(player, inventory);
                        this.inventoryDto.setOnMarket(inventory, false);

                        marketPlaces.setSold(true);
                        this.marketPlaceRepository.save(marketPlaces);

                        this.inboxDto.addInbox(
                                marketPlaces.getPlayer().getId(),
                                "Your " + marketPlaces.getInventory().getName() + " has been sold.");

                        result.setStatus(200);
                        result.setMessage("OK");
                        result.setDescription("Successfully purchased a " + marketPlaces.getInventory().getName() + ".");
                        result.setData(this.inventoryDto.toDTO(inventory));
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
            result.setStatus(400);
            result.setMessage("Bad Request");
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }


    @Override
    public ApiResponse<MarketPlaceModel> sellItem(Integer playerId, Integer itemId, double price) {
        ApiResponse<MarketPlaceModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            PlayerEntity player = this.playerDTO.findPlayerById(playerId);
            if (player == null) {
                throw new PlayerNotFoundException("Player not found with ID: " + playerId);
            }

            InventoryEntity inventory = this.inventoryDto.findInventoryById(itemId);
            if (inventory == null) {
                throw new InventoryItemNotFoundException("Inventory item not found with ID: " + itemId);
            }

            MarketPlaceEntity marketPlaces = this.marketDto.findMarketPlaceByInventoryId(inventory.getId());
            if (this.marketDto.hasOwner(player, inventory)) {
                if (this.marketDto.checkItemIdIsNotEquals(marketPlaces, inventory)) {
                    this.marketDto.addMarketPlace(player.getId(), inventory.getId(), price);
                    this.inboxDto.addInbox(
                            playerId,
                            "Your " + inventory.getName() + " has been added to the market.");

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("You have successfully added a " + inventory.getName() + " to your inventory.");
                    result.setData(this.marketDto.toDTO(
                            this.marketDto.findMarketPlaceByInventoryId(inventory.getId())));
                } else {
                    result.setStatus(400);
                    result.setMessage("Bad Request");
                    result.setDescription("You already have " + inventory.getName() + " on the market.");
                }
            } else {
                throw new UnauthorizedAccessException("Player is not the owner of the inventory item.");
            }
        } catch (PlayerNotFoundException | InventoryItemNotFoundException | UnauthorizedAccessException e) {
            result.setStatus(400);
            result.setMessage("Bad Request");
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<MarketPlaceModel> deleteMarketPlace(Integer id) {
        ApiResponse<MarketPlaceModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.marketPlaceRepository.findById(id).orElseThrow(() -> new NullPointerException("Marketplace not found!"));
            this.marketPlaceRepository.deleteById(id);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Market data has been deleted.");
            result.setData(null);
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}