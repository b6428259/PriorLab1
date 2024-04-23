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

import th.co.prior.lab1.adventureshops.model.MonsterModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.MonsterServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllMonsterTest {

    @InjectMocks
    private MonsterServiceImpl monsterServiceImpl;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterDto monsterDto;

    @Before
    public void setUp() {
        when(monsterRepository.findAll()).thenReturn(List.of(new MonsterEntity(), new MonsterEntity()));
        when(monsterDto.toDTOList(anyList())).thenReturn(List.of(new MonsterModel(), new MonsterModel()));
    }

    @Test
    public void testGetAllMonster_ShouldReturnStatusOK(){
        ApiResponse<List<MonsterModel>> result = monsterServiceImpl.getAllMonster();

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllMonster_ShouldReturnStatusNotFound(){
        when(monsterRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<List<MonsterModel>> result = monsterServiceImpl.getAllMonster();

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllMonster_ShouldThrowStatusInternalServerError(){
        when(monsterRepository.findAll()).thenThrow(new ExceptionModel());

        ApiResponse<List<MonsterModel>> result = monsterServiceImpl.getAllMonster();

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
