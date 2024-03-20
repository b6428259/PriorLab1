package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.AccountDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.PlayerModel;
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.service.PlayerService;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final AccountDto accountUtils;

    private final PlayerDto playerDTO;
    private final PlayerRepository playerRepository;

    @Override
    public ApiResponse<List<PlayerModel>> getAllPlayer() {
        ApiResponse<List<PlayerModel>> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<PlayerEntity> character = this.playerRepository.findAll();

            if (character.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved characters information.");
                result.setData(this.playerDTO.toDTOList(character));
            } else {
                result.setDescription("Character not found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<PlayerModel> getPlayerById(Integer id) {
        ApiResponse<PlayerModel> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            PlayerEntity character = playerRepository.findById(id).orElseThrow(() -> new NullPointerException("Character not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved character information.");
            result.setData(this.playerDTO.toDTO(character));
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<PlayerModel> createPlayer(String name) {
        ApiResponse<PlayerModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            Optional<PlayerEntity> duplicateCharacter = this.playerRepository.findPlayerByName(name);

            if (name.length() >= 3) {
                if (duplicateCharacter.isEmpty()) {
                    PlayerEntity character = new PlayerEntity();
                    character.setName(name);
//                   character.setLevel(this.LevelDto.getLevel());
                    PlayerEntity saved = this.playerRepository.save(character);
                    this.accountUtils.createAccount(character);

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("Successfully created character information.");
                    result.setData(this.playerDTO.toDTO(saved));
                } else {
                    result.setDescription("Duplicate name. Please enter another name.");
                }
            } else {
                result.setDescription("Please enter a name of at least 3 characters.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<PlayerModel> updatePlayer(Integer id, String name) {
        ApiResponse<PlayerModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            PlayerEntity character = this.playerRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));
            Optional<PlayerEntity> duplicateCharacterName = this.playerRepository.findPlayerByName(name);

            if (name.length() >= 3) {
                if (duplicateCharacterName.isEmpty()) {
                    character.setName(name);
                    this.playerRepository.save(character);

                    result.setStatus(200);
                    result.setMessage("OK");
                    result.setDescription("Character information has been successfully updated.");
                    result.setData(this.playerDTO.toDTO(character));
                } else {
                    result.setDescription("Duplicate name. Please enter another name.");
                }
            } else {
                result.setDescription("Please enter a name of at least 3 characters.");
            }
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}