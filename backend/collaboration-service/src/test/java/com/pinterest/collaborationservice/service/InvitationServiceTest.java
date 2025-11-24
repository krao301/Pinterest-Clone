package com.pinterest.collaborationservice.service;

import com.pinterest.collaborationservice.domain.Invitation;
import com.pinterest.collaborationservice.domain.InvitationStatus;
import com.pinterest.collaborationservice.dto.InvitationRequest;
import com.pinterest.collaborationservice.dto.InvitationResponse;
import com.pinterest.collaborationservice.exception.InvalidInvitationActionException;
import com.pinterest.collaborationservice.exception.ResourceNotFoundException;
import com.pinterest.collaborationservice.repository.InvitationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private InvitationRepository invitationRepository;

    private InvitationService invitationService;

    @BeforeEach
    void setUp() {
        invitationService = new InvitationService(invitationRepository, new ModelMapper());
    }

    @Test
    void sendInvitationPersistsPendingInvitation() {
        InvitationRequest request = new InvitationRequest();
        request.setInviterId(1L);
        request.setInviteeId(2L);
        request.setBoardId(3L);
        request.setMessage("Join my board");

        Invitation saved = Invitation.builder()
                .id(10L)
                .inviterId(1L)
                .inviteeId(2L)
                .boardId(3L)
                .message("Join my board")
                .status(InvitationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        when(invitationRepository.save(org.mockito.ArgumentMatchers.any(Invitation.class))).thenReturn(saved);

        InvitationResponse response = invitationService.sendInvitation(request);

        ArgumentCaptor<Invitation> captor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(InvitationStatus.PENDING);
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getStatus()).isEqualTo(InvitationStatus.PENDING);
    }

    @Test
    void listInvitationsFiltersByStatusWhenProvided() {
        Invitation pending = Invitation.builder().id(1L).inviteeId(2L).inviterId(3L).boardId(4L).status(InvitationStatus.PENDING).createdAt(LocalDateTime.now()).build();
        when(invitationRepository.findByInviteeIdAndStatus(2L, InvitationStatus.PENDING)).thenReturn(List.of(pending));

        List<InvitationResponse> results = invitationService.listInvitations(2L, InvitationStatus.PENDING);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(pending.getId());
        assertThat(results.get(0).getStatus()).isEqualTo(InvitationStatus.PENDING);
    }

    @Test
    void acceptUpdatesStatusWhenPending() {
        Invitation invitation = Invitation.builder()
                .id(5L)
                .inviterId(1L)
                .inviteeId(2L)
                .boardId(3L)
                .status(InvitationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        when(invitationRepository.findById(5L)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(invitation)).thenReturn(invitation);

        InvitationResponse response = invitationService.accept(5L);

        assertThat(response.getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
    }

    @Test
    void respondThrowsWhenInvitationMissing() {
        when(invitationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invitationService.accept(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Invitation not found");
    }

    @Test
    void respondThrowsWhenAlreadyProcessed() {
        Invitation invitation = Invitation.builder()
                .id(7L)
                .inviterId(1L)
                .inviteeId(2L)
                .boardId(3L)
                .status(InvitationStatus.ACCEPTED)
                .createdAt(LocalDateTime.now())
                .build();
        when(invitationRepository.findById(7L)).thenReturn(Optional.of(invitation));

        assertThatThrownBy(() -> invitationService.decline(7L))
                .isInstanceOf(InvalidInvitationActionException.class)
                .hasMessageContaining("Invitation is already");
    }
}
