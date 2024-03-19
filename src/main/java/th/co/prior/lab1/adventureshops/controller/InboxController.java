package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.service.implement.InboxServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
public class InboxController {

    private final InboxServiceImpl inboxService;

    @GetMapping("/inbox")
    public ResponseEntity<ApiResponse<List<InboxEntity>>> getInboxes() {
        ApiResponse<List<InboxEntity>> response = this.inboxService.getAllInboxes();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
