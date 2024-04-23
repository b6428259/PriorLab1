package th.co.prior.lab1.adventureshops.service.monster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.lab1.adventureshops.dto.MonsterDto;
import th.co.prior.lab1.adventureshops.entity.MonsterEntity;
import th.co.prior.lab1.adventureshops.exception.ExceptionModel;
import th.co.prior.lab1.adventureshops.repository.MonsterRepository;
import th.co.prior.lab1.adventureshops.service.implement.MonsterServiceImpl;
import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetMonsterByIdTest {

    @InjectMocks
    private MonsterServiceImpl monsterServiceImpl;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterDto monsterDto;

    @Before
    public void setUp() {
        when(monsterRepository.findById(any())).thenReturn(Optional.of(new MonsterEntity()));
        when(monsterDto.toDTO(any())).thenReturn(new MonsterModel());
    }


    @Test
    public void testGetCharacterById_ShouldReturnStatusOK() {
        ApiResponse<MonsterModel> result = monsterServiceImpl.getMonsterById(1);

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetCharacterById_ShouldReturnStatusNotFound() {
        when(monsterRepository.findById(1)).thenReturn(Optional.empty());

        ApiResponse<MonsterModel> result = monsterServiceImpl.getMonsterById(1);

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetCharacterById_ShouldThrowStatusInternalServerError() {
        when(monsterRepository.findById(1)).thenThrow(new ExceptionModel());

        ApiResponse<MonsterModel> result = monsterServiceImpl.getMonsterById(1);

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
