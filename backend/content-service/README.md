# Content Service Scaffold

Responsibilities:
- CRUD for pins and boards, including drafts, visibility controls (public/private), and attribution metadata.
- Media handling (image/video uploads or external URLs) with lazy-loading friendly metadata.
- Search and discovery APIs for pins/boards with filtering, sorting, and keyword search.
- Collaboration hooks for shared boards and invitations.
- Circuit breakers for calls to collaboration/user services; apply the 3s timeout, 50% error threshold, and 60s open window.

Implementation notes:
- Spring Data JPA with MySQL; design entities for pins, boards, keywords, and relationships.
- Use DTOs for API contracts and ModelMapper for conversions; Lombok for boilerplate.
- Add Bean Validation for titles, descriptions, URLs, and board names (no leading zero dates).
- Document APIs with Swagger/OpenAPI and secure them via Spring Security filters from the gateway.
