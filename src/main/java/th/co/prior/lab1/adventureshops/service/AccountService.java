package th.co.prior.lab1.adventureshops.service;



import org.springframework.http.ResponseEntity;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.model.AccountModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.List;

public interface AccountService {
    ApiResponse<List<AccountEntity>> getAllAccounts();


    ApiResponse<AccountModel> updateAccount(Integer id, double balance);


    ApiResponse<AccountModel> getAccountById(Integer id);

    ApiResponse<AccountModel> createAccount(Integer characterId, double balance);
}
