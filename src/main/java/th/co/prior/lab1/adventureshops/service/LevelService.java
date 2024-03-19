package th.co.prior.lab1.adventureshops.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;
import th.co.prior.lab1.adventureshops.repository.LevelRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;


@Service
public class LevelService {
    private LevelRepository repository;


    public void removeAllLevels() {
        repository.deleteAll();
    }

    @Autowired
    public LevelService(LevelRepository repository){
        this.repository = repository;
    }

    public List<LevelEntity> retrieveLevel() {
        return (List<LevelEntity>) repository.findAll();
    }

    public Optional<LevelEntity> retrieveLevel(Long id){
        return repository.findById(Math.toIntExact(id));
    }

    public LevelEntity save(LevelEntity levelEntity) {
        return repository.save(levelEntity);
    }

    public LevelEntity createLevel(LevelEntity level) {
        return repository.save(level);
    }

    public LevelEntity findById(Long id) {
        return repository.findById(Math.toIntExact(id)).orElse(null);
    }
}
