# Collaboration Service

Manages collaboration invitations, acceptance/decline flows, and collaborator permissions for shared boards.

## Available endpoints
- `POST /api/invitations` — create a collaboration invitation (inviterId, inviteeId, boardId, optional message).
- `GET /api/invitations?inviteeId={id}&status={PENDING|ACCEPTED|DECLINED|IGNORED}` — list invitations for an invitee with optional status filter.
- `POST /api/invitations/{id}/accept` — accept a pending invitation.
- `POST /api/invitations/{id}/decline` — decline a pending invitation.
- `POST /api/invitations/{id}/ignore` — mark a pending invitation as ignored.
- `GET /api/collaboration/ping` — health check for routing through the gateway.

## Notes
- Validation rejects missing IDs with friendly messages and limits messages to 255 characters.
- A centralized exception handler returns consistent error envelopes for validation errors, not-found resources, and invalid actions.
- Backed by Spring Data JPA with MySQL; update `application.yml` before running locally.
- Mockito-based unit tests cover invitation creation, listing, and response transitions.
