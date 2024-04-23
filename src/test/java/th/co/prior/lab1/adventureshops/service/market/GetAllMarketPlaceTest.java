package th.co.prior.lab1.adventureshops.service.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.lab1.adventureshops.dto.MarketDto;
import th.co.prior.lab1.adventureshops.entity.MarketPlaceEntity;
import th.co.prior.lab1.adventureshops.exception.ExceptionModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.MarketPlaceModel;
import th.co.prior.lab1.adventureshops.repository.MarketPlaceRepository;
import th.co.prior.lab1.adventureshops.service.implement.MarketPlaceServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllMarketPlaceTest {

    @InjectMocks
    private MarketPlaceServiceImpl marketPlaceServiceImpl;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;
    @Mock
    private MarketDto marketPlaceDto;

    @Before
    public void setUp() {
        when(marketPlaceRepository.findAll()).thenReturn(List.of(new MarketPlaceEntity(), new MarketPlaceEntity()));
        when(marketPlaceDto.toDTOList(anyList())).thenReturn(List.of(new MarketPlaceModel(), new MarketPlaceModel()));
    }

    @Test
    public void testGetAllMarketPlace_ShouldReturnStatusOK(){
        ApiResponse<List<MarketPlaceModel>> result = marketPlaceServiceImpl.getAllMarkerPlace();

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllMarketPlace_ShouldReturnStatusNotFound(){
        when(marketPlaceRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<List<MarketPlaceModel>> result = marketPlaceServiceImpl.getAllMarkerPlace();

        assertThat(result.getMessage()).isEqualTo("Not Found!");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllMarketPlace_ShouldThrowStatusInternalServerError(){
        when(marketPlaceRepository.findAll()).thenThrow(new RuntimeException("Internal Server Error"));

        ApiResponse<List<MarketPlaceModel>> result = marketPlaceServiceImpl.getAllMarkerPlace();

        assertThat(result.getDescription()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
