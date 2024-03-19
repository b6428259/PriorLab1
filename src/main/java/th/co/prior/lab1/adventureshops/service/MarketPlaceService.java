package th.co.prior.lab1.adventureshops.service;


import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.List;

public interface MarketPlaceService {

    ApiResponse<List<MarketPlaceEntity>> getAllMarketPlaces();

    ApiResponse<MarketPlaceEntity> getMarketPlaceById(Integer id);

    void addMarketPlace(Integer characterId, Integer inventoryId, double price);

    ApiResponse<String> sellItem(Integer playerId, Integer itemId, double price);

    ApiResponse<String> buyItem(Integer playerId, Integer itemId, double price);
}