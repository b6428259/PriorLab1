package th.co.prior.lab1.adventureshops.service;


import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;

import java.util.List;

public interface MarketPlaceService {

    ResponseModel<List<MarketPlaceEntity>> getAllMarketPlaces();

    ResponseModel<MarketPlaceEntity> getMarketPlaceById(Integer id);

    void addMarketPlace(Integer characterId, Integer inventoryId, double price);

    ResponseModel<String> sellItem(Integer playerId, Integer itemId, double price);

    ResponseModel<String> buyItem(Integer playerId, Integer itemId, double price);
}