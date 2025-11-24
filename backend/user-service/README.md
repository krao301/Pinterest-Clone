# User Service Scaffold

Responsibilities:
- Registration, login, logout, and session management with Spring Security and OAuth2/OIDC if required.
- User profiles, followers/following lists, invitations, and connection management.
- Validation using Bean Validation plus custom validators for passwords, email, and usernames.
- Resilience4j-based login attempt throttling (open circuit for 60s after 10 failures within 30s window, 3s timeout, 50% error threshold).
- DTO layering via ModelMapper and Lombok for data classes.
- Centralized exception handling with descriptive error messages.

Implementation notes:
- Use Spring Data JPA with MySQL; store passwords using strong hashing.
- Provide Swagger/OpenAPI documentation for all endpoints.
- Include unit tests with Mockito targeting at least 80% coverage.
