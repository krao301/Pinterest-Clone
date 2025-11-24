# Content Service

Responsibilities:
- CRUD for pins and boards, including drafts, visibility controls (public/private), and attribution metadata.
- Media handling (image/video uploads or external URLs) with lazy-loading friendly metadata.
- Search and discovery APIs for pins/boards with filtering, sorting, and keyword search.
- Collaboration hooks for shared boards and invitations.
- Circuit breakers for calls to collaboration/user services; apply the 3s timeout, 50% error threshold, and 60s open window.

## Current API surface
- `POST /api/boards` – create a board with visibility, cover image, and owner metadata (validated)
- `GET /api/boards` – list boards with optional `q` (name search) and `visibility` filters
- `GET /api/boards/{id}` – fetch a single board with pin count
- `POST /api/pins` – create a pin, attach to a board, set draft/visible flags, and provide keywords/source URL
- `GET /api/pins` – search pins by keyword/title/description or restrict to a `boardId`
- `GET /api/pins/{id}` – fetch a single pin with board metadata
- `DELETE /api/pins/{id}` – remove a pin you own

## Implementation notes
- Spring Data JPA with MySQL; entities for pins, boards, keywords (comma-separated), and relationships.
- Uses DTOs for API contracts and ModelMapper for conversions; Lombok for boilerplate.
- Bean Validation applied to titles, descriptions, URLs, and board names (no leading zero dates).
- Exceptions unified through `GlobalExceptionHandler` returning timestamped error payloads.
- Document APIs with Swagger/OpenAPI and secure them via Spring Security filters from the gateway.

## Testing
- `mvn -pl backend/content-service test`
