package com.pinterest.collaborationservice.service;

import com.pinterest.collaborationservice.domain.Invitation;
import com.pinterest.collaborationservice.domain.InvitationStatus;
import com.pinterest.collaborationservice.dto.InvitationRequest;
import com.pinterest.collaborationservice.dto.InvitationResponse;
import com.pinterest.collaborationservice.exception.InvalidInvitationActionException;
import com.pinterest.collaborationservice.exception.ResourceNotFoundException;
import com.pinterest.collaborationservice.repository.InvitationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final ModelMapper modelMapper;

    public InvitationService(InvitationRepository invitationRepository, ModelMapper modelMapper) {
        this.invitationRepository = invitationRepository;
        this.modelMapper = modelMapper;
    }

    public InvitationResponse sendInvitation(InvitationRequest request) {
        Invitation invitation = modelMapper.map(request, Invitation.class);
        invitation.setStatus(InvitationStatus.PENDING);
        Invitation saved = invitationRepository.save(invitation);
        return modelMapper.map(saved, InvitationResponse.class);
    }

    public List<InvitationResponse> listInvitations(Long inviteeId, InvitationStatus status) {
        List<Invitation> invitations = status == null
                ? invitationRepository.findByInviteeId(inviteeId)
                : invitationRepository.findByInviteeIdAndStatus(inviteeId, status);
        return invitations.stream()
                .map(invitation -> modelMapper.map(invitation, InvitationResponse.class))
                .collect(Collectors.toList());
    }

    public InvitationResponse accept(Long invitationId) {
        return respond(invitationId, InvitationStatus.ACCEPTED);
    }

    public InvitationResponse decline(Long invitationId) {
        return respond(invitationId, InvitationStatus.DECLINED);
    }

    public InvitationResponse ignore(Long invitationId) {
        return respond(invitationId, InvitationStatus.IGNORED);
    }

    private InvitationResponse respond(Long invitationId, InvitationStatus targetStatus) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + invitationId));
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new InvalidInvitationActionException("Invitation is already " + invitation.getStatus());
        }
        invitation.setStatus(targetStatus);
        Invitation saved = invitationRepository.save(invitation);
        return modelMapper.map(saved, InvitationResponse.class);
    }
}
