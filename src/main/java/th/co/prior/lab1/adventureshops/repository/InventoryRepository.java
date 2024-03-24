package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.lab1.adventureshops.entity.InventoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {
    List<InventoryEntity> findByName(String name);
}
