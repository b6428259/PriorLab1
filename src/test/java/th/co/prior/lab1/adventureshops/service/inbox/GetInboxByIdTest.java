package th.co.prior.lab1.adventureshops.service.inbox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.lab1.adventureshops.dto.InboxDto;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.exception.ExceptionModel;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InboxModel;
import th.co.prior.lab1.adventureshops.repository.InboxRepository;
import th.co.prior.lab1.adventureshops.service.implement.InboxServiceImpl;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetInboxByIdTest {

    @InjectMocks
    private InboxServiceImpl inboxService;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private InboxDto inboxUtils;

    @Before
    public void setUp() {
        when(inboxRepository.findById(any())).thenReturn(Optional.of(new InboxEntity()));
        when(inboxUtils.toDTO(any())).thenReturn(new InboxModel());
    }


    @Test
    public void testGetInboxById_ShouldReturnStatusOK() {
        ApiResponse<InboxModel> result = inboxService.getInboxById(1);

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetInboxById_ShouldReturnStatusNotFound() {
        when(inboxRepository.findById(1)).thenReturn(Optional.empty());

        ApiResponse<InboxModel> result = inboxService.getInboxById(1);

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetInboxById_ShouldThrowStatusInternalServerError() {
        when(inboxRepository.findById(1)).thenThrow(new ExceptionModel());

        ApiResponse<InboxModel> result = inboxService.getInboxById(1);

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
