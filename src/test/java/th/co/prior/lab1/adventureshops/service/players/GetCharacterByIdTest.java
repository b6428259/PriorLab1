package th.co.prior.lab1.adventureshops.service.players;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.lab1.adventureshops.dto.PlayerDto;
import th.co.prior.lab1.adventureshops.entity.PlayerEntity;
import th.co.prior.lab1.adventureshops.exception.ExceptionModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.PlayerModel;
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.service.implement.PlayerServiceImpl;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetCharacterByIdTest {

    @InjectMocks
    private PlayerServiceImpl playerServiceImpl;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerDto playerDto;

    @Before
    public void setUp() {
        when(playerRepository.findById(any())).thenReturn(Optional.of(new PlayerEntity()));
        when(playerDto.toDTO(any())).thenReturn(new PlayerModel());
    }


    @Test
    public void testGetCharacterById_ShouldReturnStatusOK() {
        ApiResponse<PlayerModel> result = playerServiceImpl.getPlayerById(1);

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetCharacterById_ShouldReturnStatusNotFound() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        ApiResponse<PlayerModel> result = playerServiceImpl.getPlayerById(1);

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetCharacterById_ShouldThrowStatusInternalServerError() {
        when(playerRepository.findById(1)).thenThrow(new ExceptionModel());

        ApiResponse<PlayerModel> result = playerServiceImpl.getPlayerById(1);

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
