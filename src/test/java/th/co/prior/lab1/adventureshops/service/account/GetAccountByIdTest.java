package th.co.prior.lab1.adventureshops.service.account;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.lab1.adventureshops.dto.AccountDto;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.exception.ExceptionModel;
import th.co.prior.lab1.adventureshops.model.AccountModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;
import th.co.prior.lab1.adventureshops.service.implement.AccountServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
@RunWith(MockitoJUnitRunner.class)
public class GetAccountByIdTest {

    @InjectMocks
    private AccountServiceImpl accountServiceImp;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountDto accountdto;

    @Before
    public void setUp() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity()));
        when(accountdto.toDTO(any())).thenReturn((new AccountModel()));
    }


    @Test
    public void testGetAccountById_ShouldReturnStatusOK() {


        ApiResponse<AccountModel> result = accountServiceImp.getAccountById(1);

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetAccountById_ShouldReturnStatusNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        ApiResponse<AccountModel> result = accountServiceImp.getAccountById(1);

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAccountById_ShouldThrowStatusInternalServerError() {
        when(accountRepository.findById(1)).thenThrow(new ExceptionModel());

        ApiResponse<AccountModel> result = accountServiceImp.getAccountById(1);

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
