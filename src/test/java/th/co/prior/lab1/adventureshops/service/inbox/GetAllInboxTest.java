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


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllInboxTest {

    @InjectMocks
    private InboxServiceImpl inboxService;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private InboxDto inboxdto;

    @Before
    public void setUp() {
        when(inboxRepository.findAll()).thenReturn(List.of(new InboxEntity(), new InboxEntity()));
        when(inboxdto.toDTOList(anyList())).thenReturn(List.of(new InboxModel(), new InboxModel()));
    }

    @Test
    public void testGetAllInbox_ShouldReturnStatusOK() {
        ApiResponse<List<InboxModel>> result = inboxService.getAllInbox();

        assertThat(result.getMessage()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllInbox_ShouldReturnStatusNotFound() {
        when(inboxRepository.findAll()).thenReturn(new ArrayList<>());

        ApiResponse<List<InboxModel>> result = inboxService.getAllInbox();

        assertThat(result.getMessage()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllInbox_ShouldThrowStatusInternalServerError() {
        when(inboxRepository.findAll()).thenThrow(new ExceptionModel());

        ApiResponse<List<InboxModel>> result = inboxService.getAllInbox();

        assertThat(result.getMessage()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
