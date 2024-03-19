package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;
import th.co.prior.lab1.adventureshops.service.AccountService;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    @Override
    public ApiResponse<List<AccountEntity>> getAllAccounts() {
        ApiResponse<List<AccountEntity>> result = new ApiResponse<>();
        try {
            List<AccountEntity> accounts = this.accountRepository.findAll();
            if (!accounts.isEmpty()) {
                result.setStatus(200);
                result.setDescription("List of all accounts retrieved successfully.");
                result.setData(accounts);
            } else {
                result.setStatus(404);
                result.setDescription("No Accounts Found!");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public void depositBalance(Integer id, double balance) {
        try {
            AccountEntity account = getAccountById(id);
            account.setBalance(account.getBalance() + balance);
            this.accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error("Error depositing balance for account with id {}: {}", id, e.getMessage());
        }
    }

    @Override
    public void withdrawBalance(Integer id, double balance) {
        try {
            AccountEntity account = getAccountById(id);
            double newBalance = account.getBalance() - balance;
            if (newBalance < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            account.setBalance(newBalance);
            this.accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error("Error withdrawing balance for account with id {}: {}", id, e.getMessage());
        }
    }

    private AccountEntity getAccountById(Integer id) {
        return this.accountRepository.findAccountByPlayerId(id)
                .orElseThrow(() -> new RuntimeException("Account not found for id: " + id));
    }
}
