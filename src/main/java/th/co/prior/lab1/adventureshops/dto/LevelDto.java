package th.co.prior.lab1.adventureshops.dto;



import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.repository.LevelRepository;


@Component
@AllArgsConstructor
public class LevelDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelDto.class);
    private final LevelRepository levelRepository;

    public LevelEntity getLevel() {
        return this.levelRepository.getReferenceById(1L);
    }
}