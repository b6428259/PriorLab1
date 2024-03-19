package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;


import java.util.Optional;

@Repository
public interface MonsterRepository extends JpaRepository<MonsterEntity, Integer> {
    Optional<MonsterEntity> findMonsterByName(String name);

    Optional<MonsterEntity> findMonsterEntityById(Integer id);

    Optional<MonsterEntity> findMonsterByItemDrop(String itemDrop);

}
