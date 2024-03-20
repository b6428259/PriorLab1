package th.co.prior.lab1.adventureshops.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import th.co.prior.lab1.adventureshops.dto.AccountDto;
import th.co.prior.lab1.adventureshops.dto.EntityDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
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
    private final PlayerDto playerDTO;
    private final EntityDto entityDTO;

    @Override
    public ApiResponse<List<AccountEntity>> getAllAccounts() {
        ApiResponse<List<AccountEntity>> result = new ApiResponse<>();
        try {
            List<AccountEntity> accounts = accountRepository.findAll();
            if (!accounts.isEmpty()) {
                result.setStatus(HttpStatus.OK.value());
                result.setDescription("List of all accounts retrieved successfully.");
                result.setData(accounts);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setDescription("No Accounts Found!");
            }
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ApiResponse<AccountModel> updateAccount(Integer id, double balance) {
        ApiResponse<AccountModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Bad Request");

        try {
            AccountEntity account = accountRepository.findById(id)
                    .orElseThrow(() -> new NullPointerException("Account not found!"));

            account.setBalance(balance);
            accountRepository.save(account);

            result.setStatus(HttpStatus.OK.value());
            result.setMessage("OK");
            result.setDescription("Account information has been successfully updated.");
            result.setData(accountDTO.toDTO(account));
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<AccountModel> getAccountById(Integer id) {
        ApiResponse<AccountModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.NOT_FOUND.value());
        result.setMessage("Not Found");

        try {
            AccountEntity account = accountRepository.findById(id)
                    .orElseThrow(() -> new NullPointerException("Account not found!"));

            result.setStatus(HttpStatus.OK.value());
            result.setMessage("OK");
            result.setDescription("Successfully retrieved accounts information.");
            result.setData(accountDTO.toDTO(account));
        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ApiResponse<AccountModel> createAccount(Integer characterId, double balance) {
        ApiResponse<AccountModel> result = new ApiResponse<>();
        result.setStatus(HttpStatus.BAD_REQUEST.value());
        result.setMessage("Bad Request");

        try {
            Optional<AccountEntity> duplicateAccount = accountRepository.findAccountByPlayerId(characterId);
            PlayerEntity player = playerDTO.findPlayerById(characterId);

            if (entityDTO.hasEntity(player)) {
                if (duplicateAccount.isEmpty()) {
                    AccountEntity account = new AccountEntity();
                    account.setAccountNumber(accountDTO.getAccountNumber());
                    account.setBalance(balance);
                    account.setPlayer(player);
                    AccountEntity saved = accountRepository.save(account);

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setMessage("Created");
                    result.setDescription("Successfully created player " + player + " information.");
                    result.setData(accountDTO.toDTO(saved));
                } else {
                    result.setDescription(player + " already have an account.");
                }
            } else {
                result.setDescription("Player " + player + " not found.");
            }
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
