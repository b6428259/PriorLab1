package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import th.co.prior.lab1.adventureshops.entity.LevelEntity;

import java.util.List;

@EnableRedisRepositories
public interface LevelRepository extends CrudRepository<LevelEntity, Integer > {

    List<LevelEntity> findLevelEntityByDamage(Long level);
}
