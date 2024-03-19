package th.co.prior.lab1.adventureshops.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.lab1.adventureshops.entity.InboxEntity;
import th.co.prior.lab1.adventureshops.model.ApiResponse;
import th.co.prior.lab1.adventureshops.model.InboxModel;
import th.co.prior.lab1.adventureshops.service.InboxService;
import th.co.prior.lab1.adventureshops.service.implement.InboxServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/inbox")
public class InboxController {

    private final InboxService inboxService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InboxModel>>> getInbox() {
        ApiResponse<List<InboxModel>> response = this.inboxService.getAllInbox();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InboxModel>> getInboxById(@PathVariable Integer id){
        ApiResponse<InboxModel> response = this.inboxService.getInboxById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<InboxModel>> createInbox(
            @RequestParam Integer characterId,
            @RequestParam String message
    ) {
        ApiResponse<InboxModel> response = inboxService.createInbox(characterId, message);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<InboxModel>> updateInbox(
            @PathVariable Integer id,
            @RequestParam String message
    ) {
        ApiResponse<InboxModel> response = inboxService.updateInbox(id, message);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
