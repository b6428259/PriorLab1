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
import th.co.prior.lab1.adventureshops.repository.PlayerRepository;
import th.co.prior.lab1.adventureshops.service.implement.PlayerServiceImpl;
import th.co.prior.lab1.adventureshops.model.PlayerModel;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllCharacterTest {

    @InjectMocks
    private PlayerServiceImpl playerServiceImpl;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerDto playerDto;

    @Before
    public void setUp() {
        when(playerRepository.findAll()).thenReturn(List.of(new PlayerEntity(), new PlayerEntity()));
        when(playerDto.toDTOList(anyList())).thenReturn(List.of(new PlayerModel(), new PlayerModel()));
    }

    @Test
    public void testGetAllCharacter_ShouldReturnStatusOK(){
        ApiResponse<List<PlayerModel>> result = playerServiceImpl.getAllPlayer();

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllCharacter_ShouldReturnStatusNotFound(){
        when(playerRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<List<PlayerModel>> result = playerServiceImpl.getAllPlayer();

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllCharacter_ShouldThrowStatusInternalServerError(){
        when(playerRepository.findAll()).thenThrow(new ExceptionModel());

        ApiResponse<List<PlayerModel>> result = playerServiceImpl.getAllPlayer();

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
