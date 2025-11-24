package com.pinterest.collaborationservice.repository;

import com.pinterest.collaborationservice.domain.Invitation;
import com.pinterest.collaborationservice.domain.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInviteeIdAndStatus(Long inviteeId, InvitationStatus status);
    List<Invitation> findByInviteeId(Long inviteeId);
}
