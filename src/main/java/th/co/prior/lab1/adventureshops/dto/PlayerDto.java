package th.co.prior.lab1.adventureshops.dto;



import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.PlayerModel;
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.service.LevelService;


import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PlayerDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDto.class);
    private final PlayerRepository playerRepository;
    private final LevelService levelService;

    public List<PlayerModel> toDTOList(List<PlayerEntity> character) {
        return character.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PlayerModel toDTO(PlayerEntity character) {
        PlayerModel dto = new PlayerModel();
        dto.setId(character.getId());
        dto.setName(character.getName());

        // Check if the player's level ID is not null before converting
        if (character.getLevelId() != null) {
            dto.setLevel(Math.toIntExact(character.getLevelId()));

            // Fetch the level entity only if the level ID is not null
            LevelEntity levelEntity = levelService.findById(Long.valueOf(character.getLevelId()));
            if (levelEntity != null) {
                dto.setDamage(levelEntity.getDamage());
            }
        }

        return dto;
    }

    public List<PlayerEntity> findAllPlayer(){
        return playerRepository.findAll();
    }

    public PlayerEntity findPlayerById(Integer id){
        return playerRepository.findById(id).orElse(null);
    }

    public PlayerEntity findPlayerByName(String name) { return playerRepository.findPlayerByName(name).orElse(null); }
}