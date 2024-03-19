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

    public List<AccountModel> toDTOList(List<AccountEntity> account) {
        return account.stream()
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

    public List<AccountEntity> findAllAccount(){
        return accountRepository.findAll();
    }

    public AccountEntity findAccountById(Integer id){
        return accountRepository.findById(id).orElse(null);
    }

    public AccountEntity findAccountByCharacterId(Integer characterId){
        return this.accountRepository.findAccountByPlayerId(characterId).orElse(null);
    }

    public void createAccount(PlayerEntity player) {
        AccountEntity account = new AccountEntity();
        account.setAccountNumber(this.getAccountNumber());
        account.setBalance(3000.00);
        account.setPlayer(player);
        this.accountRepository.save(account);

    }

    public void depositBalance(Integer id, double balance) {
        try {
            AccountEntity account = this.accountRepository.findAccountByPlayerId(id).orElseThrow(() -> new RuntimeException("Can't deposit balance because character not found!"));

            double total = this.formatDecimal(account.getBalance() + balance);
            account.setBalance(total);
            this.accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    public void withdrawBalance(Integer id, double balance) {
        try {
            AccountEntity account = this.accountRepository.findAccountByPlayerId(id).orElseThrow(() -> new RuntimeException("Can't withdraw balance because character not found!"));

            double total = this.formatDecimal(account.getBalance() - balance);
            account.setBalance(total);
            this.accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }

    }

    public String getAccountNumber(){
        StringBuilder start = new StringBuilder();
        Random value = new Random();

        int count = 0;
        int n = 0;

        for (int i = 0; i < 19; i++) {
            if (count == 4) {
                start.append(" ");
                count = 0;
            } else {
                n = value.nextInt(10);
                start.append(n);
                count++;
            }
        }

        return start.toString();
    }

    public double formatDecimal(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        String balance = df.format(value);

        return Double.parseDouble(balance);
    }
}