package th.co.prior.lab1.adventureshops.service.implement;

import jakarta.persistence.EntityNotFoundException;
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AccountDto accountDTO;
    private final PlayerDto playerDTO;
    private final EntityDto entityDTO;

    @Override
    public ApiResponse<List<AccountModel>> getAllAccounts() {
        ApiResponse<List<AccountModel>> result = new ApiResponse<>();
        try {
            List<AccountEntity> entities = accountRepository.findAll();
            List<AccountModel> models = entities.stream()
                    .map(this::convertToModel)
                    .collect(Collectors.toList());
            if (!models.isEmpty()) {
                result.setStatus(HttpStatus.OK.value());
                result.setMessage("OK");
                result.setDescription("List of all accounts retrieved successfully.");
                result.setData(models);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setMessage("Not Found");
                result.setDescription("No Accounts Found!");
                result.setData(new ArrayList<>()); // Set an empty list here
            }
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }
        return result;
    }







    private AccountModel convertToModel(AccountEntity entity) {
        AccountModel model = new AccountModel();
        model.setId(entity.getId());

        // Check if player is not null before accessing its methods
        if (entity.getPlayer() != null) {
            model.setBuyerName(entity.getPlayer().getName()); // Assuming 'getName()' is your buyer's name in the entity
        } else {
            model.setBuyerName(""); // Set a default value or handle accordingly
        }

        model.setAccountNum(entity.getAccountNumber()); // Adjust method name as per your entity
        model.setBalance(entity.getBalance());
        return model;
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
        LOGGER.debug("Attempting to retrieve account with ID {}", id);
        ApiResponse<AccountModel> result = new ApiResponse<>();

        try {
            AccountEntity account = accountRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found for ID: " + id));

            result.setStatus(HttpStatus.OK.value());
            result.setMessage("OK");
            result.setDescription("Successfully retrieved account information.");
            result.setData(accountDTO.toDTO(account));
            LOGGER.info("Account retrieved successfully for ID {}", id);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Failed to retrieve account: {}", e.getMessage());
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setMessage("Not Found");
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Internal server error while retrieving account: {}", e.getMessage());
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setMessage("Internal Server Error");
            result.setDescription("An unexpected error occurred.");
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
                    account.setAccountNumber(accountDTO.generateAccountNumber());
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
