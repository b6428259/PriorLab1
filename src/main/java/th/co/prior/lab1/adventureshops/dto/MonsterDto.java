package th.co.prior.lab1.adventureshops.dto;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.repository.MonsterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MonsterDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonsterDto.class);
    private final MonsterRepository monsterRepository;

    public List<MonsterModel> toDTOList(List<MonsterEntity> monster) {
        return monster.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MonsterModel toDTO(MonsterEntity monster){
        MonsterModel dto = new MonsterModel();
        dto.setId(monster.getId());
        dto.setName(monster.getName());
        dto.setMaxHealth(monster.getHealth());
        dto.setDropItem(monster.getItemDrop());

        return dto;
    }

    public List<MonsterEntity> findAllMonster(){
        return monsterRepository.findAll();
    }

    public MonsterEntity findMonsterById(Integer id){
        return monsterRepository.findById(id).orElse(null);
    }

    public MonsterEntity findMonsterByName(String name) { return monsterRepository.findMonsterByName(name).orElse(null); }
}