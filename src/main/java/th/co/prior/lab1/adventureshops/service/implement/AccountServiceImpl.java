package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.AccountDto;
import th.co.prior.lab1.adventureshops.dto.EntityDTO;
import th.co.prior.lab1.adventureshops.dto.PlayerDTO;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.AccountModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;
import th.co.prior.lab1.adventureshops.service.AccountService;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AccountDto accountDTO;
    private final PlayerDTO playerDTO;
    private final EntityDTO entityDTO;


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
    public ApiResponse<AccountModel> updateAccount(Integer id, double balance) {
        ApiResponse<AccountModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            AccountEntity account = this.accountRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));

            account.setBalance(balance);
            this.accountRepository.save(account);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Account information has been successfully updated.");
            result.setData(this.accountDTO.toDTO(account));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }


    @Override
    public ApiResponse<AccountModel> getAccountById(Integer id) {
        ApiResponse<AccountModel> result = new ApiResponse<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            AccountEntity account = this.accountRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved account information.");
            result.setData(this.accountDTO.toDTO(account));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
    @Override
    public ApiResponse<AccountModel> createAccount(Integer characterId, double balance) {
        ApiResponse<AccountModel> result = new ApiResponse<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            Optional<AccountEntity> duplicateAccount = this.accountRepository.findAccountByPlayerId(characterId);
            PlayerEntity player = this.playerDTO.findPlayerById(characterId);

            if(this.entityDTO.hasEntity(player)) {
                if(duplicateAccount.isEmpty()) {
                    AccountEntity account = new AccountEntity();
                        account.setAccountNumber(this.accountDTO.getAccountNumber());
                    account.setBalance(balance);
                    account.setPlayer(player);
                    AccountEntity saved = this.accountRepository.save(account);

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("Successfully created player "+player+" information.");
                    result.setData(this.accountDTO.toDTO(saved));
                } else {
                    result.setDescription(player + " already have an account.");
                }
            } else {
                result.setDescription("Player "+ player +"not found.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

