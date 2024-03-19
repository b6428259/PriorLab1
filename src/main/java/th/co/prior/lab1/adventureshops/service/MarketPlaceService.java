package th.co.prior.lab1.adventureshops.service;


import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InventoryModel;
import th.co.prior.lab1.adventureshops.model.MarketPlaceModel;

import java.util.List;

import java.util.List;

public interface MarketPlaceService {

    ApiResponse<List<MarketPlaceModel>> getAllMarkerPlace();

    ApiResponse<MarketPlaceModel> getMarketPlaceById(Integer id);

    ApiResponse<InventoryModel> buyItem(Integer characterId, Integer itemId);

    ApiResponse<MarketPlaceModel> sellItem(Integer characterId, Integer itemId, double price);

    ApiResponse<MarketPlaceModel> deleteMarketPlace(Integer id);
}