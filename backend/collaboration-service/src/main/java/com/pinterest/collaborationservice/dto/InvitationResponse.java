package com.pinterest.collaborationservice.dto;

import com.pinterest.collaborationservice.domain.InvitationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvitationResponse {
    private Long id;
    private Long inviterId;
    private Long inviteeId;
    private Long boardId;
    private String message;
    private InvitationStatus status;
    private LocalDateTime createdAt;
}
