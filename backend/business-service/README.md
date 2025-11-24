# Business Service Scaffold

Responsibilities:
- Manage business profiles, showcases, sponsored pins, and advertising campaigns.
- Provide search and filtering endpoints dedicated to business content.
- Track analytics metadata required for sponsored content and expose it via APIs.
- Apply circuit breakers on external calls and integrate with gateway security rules.

Implementation notes:
- Build with Spring Boot, Spring Data JPA (MySQL), and Spring REST.
- Use DTOs and ModelMapper; rely on Lombok for entities and logging.
- Include Bean Validation for profile details and campaign definitions (no leading zero dates).
- Document endpoints with Swagger/OpenAPI and cover service logic with Mockito tests.
