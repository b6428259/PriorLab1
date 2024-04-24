package th.co.prior.lab1.adventureshops.service.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import th.co.prior.lab1.adventureshops.dto.AccountDto;
import th.co.prior.lab1.adventureshops.dto.EntityDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.model.AccountModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;
import th.co.prior.lab1.adventureshops.service.implement.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
class GetAllAccountTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountDto accountDto;

    @Mock
    private PlayerDto playerDto;

    @Mock
    private EntityDto entityDto;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testGetAllAccounts() {
        // Arrange
        AccountEntity account1 = new AccountEntity();
        AccountEntity account2 = new AccountEntity();
        List<AccountEntity> accounts = Arrays.asList(account1, account2);
        when(accountRepository.findAll()).thenReturn(accounts);



        // Act
        ApiResponse<List<AccountModel>> response = accountService.getAllAccounts();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals("List of all accounts retrieved successfully.", response.getDescription());
        assertEquals(accounts.size(), response.getData().size());
    }

    @Test
    void testGetAllAccounts_NoAccountsFound() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ApiResponse<List<AccountModel>> response = accountService.getAllAccounts();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("No Accounts Found!", response.getDescription());
        assertEquals(0, response.getData().size());
    }

    // Add more test cases for other methods similarly

}
