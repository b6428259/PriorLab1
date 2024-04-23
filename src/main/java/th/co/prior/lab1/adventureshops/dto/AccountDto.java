package th.co.prior.lab1.adventureshops.dto;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.AccountModel;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDto.class);
    private final AccountRepository accountRepository;

    public List<AccountModel> toDTOList(List<AccountEntity> accounts) {
        return accounts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AccountModel toDTO(AccountEntity account){
        AccountModel dto = new AccountModel();
        dto.setId(account.getId());
        dto.setBuyerName(account.getPlayer().getName());
        dto.setAccountNum(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        return dto;
    }

    public List<AccountEntity> findAllAccounts(){
        return accountRepository.findAll();
    }

    public AccountEntity findAccountById(Integer id){
        return accountRepository.findById(id).orElse(null);
    }

    public AccountEntity findAccountByPlayerId(Integer playerId){
        return accountRepository.findAccountByPlayerId(playerId).orElse(null);
    }

    public void createAccount(PlayerEntity player) {
        AccountEntity account = new AccountEntity();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(5000.00);
        account.setPlayer(player);
        accountRepository.save(account);
    }

    public void depositBalance(Integer playerId, double balance) {
        AccountEntity account = findAccountByPlayerId(playerId);
        if (account == null) {
            LOGGER.error("Can't deposit balance because account not found for player with ID: {}", playerId);
            return;
        }

        double total = formatDecimal(account.getBalance() + balance);
        account.setBalance(total);
        accountRepository.save(account);
    }

    public void withdrawBalance(Integer playerId, double balance) {
        AccountEntity account = findAccountByPlayerId(playerId);
        if (account == null) {
            LOGGER.error("Can't withdraw balance because account not found for player with ID: {}", playerId);
            return;
        }

        double total = formatDecimal(account.getBalance() - balance);
        account.setBalance(total);
        accountRepository.save(account);
    }

    public String generateAccountNumber(){
        StringBuilder accountNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 19; i++) {
            if (i > 0 && i % 4 == 0) {
                accountNumber.append(" ");
            }
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }

    double formatDecimal(double value){
        return Math.round(value * 100.0) / 100.0;
    }
}
