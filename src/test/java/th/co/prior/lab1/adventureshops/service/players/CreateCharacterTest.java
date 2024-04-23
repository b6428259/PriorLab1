package th.co.prior.lab1.adventureshops.service.players;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.lab1.adventureshops.dto.AccountDto;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.AccountEntity;
import th.co.prior.lab1.adventureshops.repository.AccountRepository;
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.model.PlayerModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.PlayerServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateCharacterTest {

    @InjectMocks
    private PlayerServiceImpl playerServiceImpl;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PlayerDto playerDto;
    @Mock
    private AccountDto accountDto;

    @Before
    public void setUp() {
        when(playerRepository.findPlayerByName("cwpd")).thenReturn(Optional.empty());
        when(playerRepository.save(any())).thenReturn(new PlayerEntity());
        when(playerDto.toDTO(any())).thenReturn(new PlayerModel());
    }

    @Test
    public void testCreateCharacter_ShouldReturnStatusCreated() {

        ApiResponse<PlayerModel> result = playerServiceImpl.createPlayer("cwpd");

        assertThat(result.getMessage()).isEqualTo("Created");
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testCreateCharacter_ShouldReturnStatusBadRequest() {
        when(playerRepository.findPlayerByName("cwpd")).thenReturn(Optional.of(new PlayerEntity()));

        ApiResponse<PlayerModel> result = playerServiceImpl.createPlayer("cwpd");

        assertThat(result.getMessage()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testCreateCharacter_ShouldThrowStatusInternalServerError() {
        when(playerRepository.save(any())).thenThrow(new RuntimeException("Internal Server Error"));

        ApiResponse<PlayerModel> result = playerServiceImpl.createPlayer("cwpd");

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }



}
