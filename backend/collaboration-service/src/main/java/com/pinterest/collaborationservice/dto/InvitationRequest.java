package com.pinterest.collaborationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationRequest {

    @NotNull(message = "Please provide a valid inviterId")
    private Long inviterId;

    @NotNull(message = "Please provide a valid inviteeId")
    private Long inviteeId;

    @NotNull(message = "Please provide a valid boardId")
    private Long boardId;

    @Size(max = 255, message = "Message cannot exceed 255 characters")
    private String message;
}
