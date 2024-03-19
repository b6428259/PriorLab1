package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {

    @EntityGraph(attributePaths = "level") // Fetch the associated level entity
    Optional<PlayerEntity> findPlayerById(Integer id);
    Optional<PlayerEntity> findPlayerByName(String name);
}
