# User Service Scaffold

Responsibilities:
- Registration, login, logout, and session management with Spring Security and OAuth2/OIDC if required.
- User profiles, followers/following lists, invitations, and connection management.
- Validation using Bean Validation plus custom validators for passwords, email, and usernames.
- Resilience4j-based login attempt throttling (open circuit for 60s after 3 failures within a 30s window, 3s timeout, 50% error threshold).
- DTO layering via ModelMapper and Lombok for data classes.
- Centralized exception handling with descriptive error messages.

Implementation notes:
- Use Spring Data JPA with MySQL; store passwords using strong hashing.
- Provide Swagger/OpenAPI documentation for all endpoints.
- Include unit tests with Mockito targeting at least 80% coverage.

### Current endpoints
- `POST /api/users/register` — create a new user account with email, username, password, and confirm password validation.
- `POST /api/users/login` — authenticate with email/password and return a basic user payload.
- `POST /api/users/{followerId}/follow/{followedId}` — create a follow relationship; rejects duplicates and self-follow.
- `DELETE /api/users/{followerId}/follow/{followedId}` — remove a follow relationship.
- `POST /api/users/{userId}/block/{followerId}` — mark a follower as blocked to prevent interactions.
- `GET /api/users/{userId}/followers` — list connections following the user.
- `GET /api/users/{userId}/following` — list connections the user follows.
- `GET /api/users/ping` — health check for service wiring.
