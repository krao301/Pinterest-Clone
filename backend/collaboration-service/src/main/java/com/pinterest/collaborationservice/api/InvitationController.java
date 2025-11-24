package com.pinterest.collaborationservice.api;

import com.pinterest.collaborationservice.domain.InvitationStatus;
import com.pinterest.collaborationservice.dto.InvitationRequest;
import com.pinterest.collaborationservice.dto.InvitationResponse;
import com.pinterest.collaborationservice.service.InvitationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationResponse> create(@Valid @RequestBody InvitationRequest request) {
        return ResponseEntity.ok(invitationService.sendInvitation(request));
    }

    @GetMapping
    public ResponseEntity<List<InvitationResponse>> list(@RequestParam Long inviteeId,
                                                         @RequestParam(required = false) String status) {
        InvitationStatus invitationStatus = null;
        if (status != null) {
            try {
                invitationStatus = InvitationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported status filter: " + status);
            }
        }
        return ResponseEntity.ok(invitationService.listInvitations(inviteeId, invitationStatus));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<InvitationResponse> accept(@PathVariable Long id) {
        return ResponseEntity.ok(invitationService.accept(id));
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<InvitationResponse> decline(@PathVariable Long id) {
        return ResponseEntity.ok(invitationService.decline(id));
    }

    @PostMapping("/{id}/ignore")
    public ResponseEntity<InvitationResponse> ignore(@PathVariable Long id) {
        return ResponseEntity.ok(invitationService.ignore(id));
    }
}
