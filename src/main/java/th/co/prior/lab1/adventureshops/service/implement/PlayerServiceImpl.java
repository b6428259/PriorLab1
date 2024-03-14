package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ResponseModel;
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.service.PlayerService;


import java.util.List;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public ResponseModel<List<PlayerEntity>> getAllPlayers() {
        ResponseModel<List<PlayerEntity>> result = new ResponseModel<>();

        try {
            List<PlayerEntity> players = playerRepository.findAll();
            if (!players.isEmpty()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(players);
            } else {
                result.setStatus(404);
                result.setDescription("Not Found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

            return result;
    }

    @Override
    public ResponseModel<PlayerEntity> getPlayerById(Integer id) {
        ResponseModel<PlayerEntity> result = new ResponseModel<>();


        try {
            PlayerEntity player = playerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Character not found!"));
            result.setStatus(200);
            result.setDescription("OK");
            result.setData(player);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
