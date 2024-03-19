package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.List;

public interface PlayerService {
    ApiResponse<List<PlayerEntity>> getAllPlayers();

    ApiResponse<PlayerEntity> getPlayerById(Integer id);

}
