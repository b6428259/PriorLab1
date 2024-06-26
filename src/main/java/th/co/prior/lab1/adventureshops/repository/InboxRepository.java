package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;


@Repository
public interface InboxRepository extends JpaRepository<InboxEntity, Integer> {
}