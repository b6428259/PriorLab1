package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.AccountServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountServiceImpl accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountEntity>>> getAccount() {
        ApiResponse<List<AccountEntity>> response = accountService.getAllAccounts();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}