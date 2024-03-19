package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.PlayerModel;

import java.util.List;

public interface PlayerService {
    ApiResponse<List<PlayerModel> > getAllPlayer();

    ApiResponse<PlayerModel> getPlayerById(Integer id);

    ApiResponse<PlayerModel> createPlayer(String name);

    ApiResponse<PlayerModel> updatePlayer(Integer id, String name);

}
