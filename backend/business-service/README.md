# Business Service Scaffold

Responsibilities:
- Manage business profiles, showcases, sponsored pins, and advertising campaigns.
- Provide search and filtering endpoints dedicated to business content.
- Track analytics metadata required for sponsored content and expose it via APIs.
- Apply circuit breakers on external calls and integrate with gateway security rules.

Implemented endpoints:
- `POST /api/business/profiles` create a business profile with categories and branding metadata.
- `GET /api/business/profiles` list profiles with optional name (`q`) or `category` filters.
- `GET /api/business/profiles/{id}` fetch a profile with showcase count.
- `POST /api/business/showcases` create a themed showcase for a business profile.
- `GET /api/business/showcases` list showcases filtered by `businessId` or `theme`.
- `GET /api/business/showcases/{id}` fetch showcase details.
- `POST /api/business/sponsored-pins` create a sponsored pin tied to a campaign.
- `GET /api/business/sponsored-pins` list sponsored pins filtered by `businessId` or `keyword`.
- `GET /api/business/sponsored-pins/{id}` fetch sponsored pin details.

Implementation notes:
- Build with Spring Boot, Spring Data JPA (MySQL), and Spring REST.
- Use DTOs and ModelMapper; rely on Lombok for entities and logging.
- Include Bean Validation for profile details and campaign definitions (no leading zero dates).
- Document endpoints with Swagger/OpenAPI and cover service logic with Mockito tests.
