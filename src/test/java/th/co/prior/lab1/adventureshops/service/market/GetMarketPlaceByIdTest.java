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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetMarketPlaceByIdTest {

    @InjectMocks
    private MarketPlaceServiceImpl marketPlaceServiceImpl;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;
    @Mock
    private MarketDto marketPlaceDto;

    @Before
    public void setUp() {
        when(marketPlaceRepository.findById(any())).thenReturn(Optional.of(new MarketPlaceEntity()));
        when(marketPlaceDto.toDTO(any())).thenReturn(new MarketPlaceModel());
    }


    @Test
    public void testGetMarketPlaceById_ShouldReturnStatusOK() {
        ApiResponse<MarketPlaceModel> result = marketPlaceServiceImpl.getMarketPlaceById(1);

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetMarketPlaceById_ShouldReturnStatusNotFound() {
        when(marketPlaceRepository.findById(1)).thenReturn(Optional.empty());

        ApiResponse<MarketPlaceModel> result = marketPlaceServiceImpl.getMarketPlaceById(1);

        assertThat(result.getMessage()).isEqualTo("Not Found!");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetMarketPlaceById_ShouldThrowStatusInternalServerError() {
        when(marketPlaceRepository.findById(1)).thenThrow(new ExceptionModel());

        ApiResponse<MarketPlaceModel> result = marketPlaceServiceImpl.getMarketPlaceById(1);

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
