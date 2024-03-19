package th.co.prior.lab1.adventureshops.service;



import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.List;

public interface AccountService {
    ApiResponse<List<AccountEntity>> getAllAccounts();

    void depositBalance(Integer id, double balance);

    void withdrawBalance(Integer id, double balance);
}
