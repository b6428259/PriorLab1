package th.co.prior.lab1.adventureshops.dto;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.MarketPlaceModel;
import th.co.prior.lab1.adventureshops.repository.MarketPlaceRepository;


import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MarketDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketDto.class);
    private final EntityDTO entityDTO;
    private final MarketPlaceRepository marketPlaceRepository;
    private final PlayerDTO playerDTO;
    private final AccountDto accountDto;
    private final InventoryDto inventoryDto;

    public List<MarketPlaceModel> toDTOList(List<MarketPlaceEntity> marketPlace) {
        return marketPlace.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MarketPlaceModel toDTO(MarketPlaceEntity marketPlace) {
        MarketPlaceModel dto = new MarketPlaceModel();
        dto.setId(marketPlace.getId());
        dto.setItem(marketPlace.getInventory().getName());
        dto.setSeller(marketPlace.getPlayer().getName());
        dto.setPrice(marketPlace.getCost());
        dto.setSoldStatus(marketPlace.isSold());

        return dto;
    }

    public List<MarketPlaceEntity> findAllMarketPlace() {
        return this.marketPlaceRepository.findAll();
    }

    public MarketPlaceEntity findMarketPlaceById(Integer id) {
        return this.marketPlaceRepository.findById(id).orElse(null);
    }

    public MarketPlaceEntity findMarketPlaceByInventoryId(Integer id) {
        return this.marketPlaceRepository.findMarketPlaceByInventoryId(id).orElse(null);
    }

    public void addMarketPlace(Integer characterId, Integer inventoryId, double price) {
        try {
            PlayerEntity character = this.playerDTO.findPlayerById(characterId);
            InventoryEntity inventory = this.inventoryDto.findInventoryById(inventoryId);

            if(this.entityDTO.hasEntity(character, inventory)) {
                double balance = this.accountDto.formatDecimal(price);

                MarketPlaceEntity marketPlace = new MarketPlaceEntity();
                marketPlace.setPlayer(character);
                marketPlace.setInventory(inventory);
                marketPlace.setCost(balance);
                this.marketPlaceRepository.save(marketPlace);
                this.inventoryDto.setOnMarket(inventory, true);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    public boolean checkItemIdIsNotEquals(MarketPlaceEntity market, InventoryEntity inventory) {
        return !this.entityDTO.hasEntity(market) || !(market.getInventory().isOnMarket() && market.getInventory().getId().equals(inventory.getId()));
    }

    public boolean hasOwner(PlayerEntity character, InventoryEntity inventory) {
        return this.entityDTO.hasEntity(character, inventory) && inventory.getPlayer().equals(character);
    }
}