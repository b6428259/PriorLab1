package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;



@EnableRedisRepositories
public interface LevelRepository extends CrudRepository<LevelEntity, Integer > {

    LevelEntity getReferenceById(Long id);
}
