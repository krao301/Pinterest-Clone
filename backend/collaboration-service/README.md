# Collaboration Service Scaffold

Responsibilities:
- Manage collaboration invitations, acceptance/decline flows, and collaborator permissions on boards.
- Provide APIs to list pending invitations with filtering and sorting.
- Enforce access control for shared boards and expose helper endpoints for content service to verify permissions.
- Circuit breakers for dependency calls with the specified thresholds (3s timeout, 50% errors, open for 60s after 10 consecutive failures).

Implementation notes:
- Use Spring Data JPA with MySQL for invitations, permissions, and audit logging.
- Apply DTOs, ModelMapper, and Lombok to keep controllers thin.
- Centralize exception handling and validation for request payloads (e.g., null/empty checks with friendly error messages).
- Include Swagger/OpenAPI documentation and Mockito-based unit tests targeting ≥80% coverage.
