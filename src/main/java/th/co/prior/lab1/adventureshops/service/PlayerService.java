package th.co.prior.lab1.adventureshops.service;

import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;

import java.util.List;

public interface PlayerService {
    ResponseModel<List<PlayerEntity>> getAllPlayers();

    ResponseModel<PlayerEntity> getPlayerById(Integer id);

}
