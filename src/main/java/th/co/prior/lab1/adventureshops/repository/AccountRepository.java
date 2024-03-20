package th.co.prior.lab1.adventureshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;


import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findAccountByPlayerId(Integer id);

    boolean existsByAccountNumber(String accountNumber);
}
