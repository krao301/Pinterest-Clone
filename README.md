# Pinterest Clone

This repository contains a clean scaffold for a Pinterest-style platform built with a Spring Boot microservice backend and a React frontend. The existing zip archive in the root was intentionally not used to allow a fresh implementation that follows the requirements listed in `Pinterest-QP.txt`.

## Project layout
- `backend/`: Service-by-service scaffolding for the Spring ecosystem implementation.
  - `gateway-service/`: Entry point for routing, load balancing, and cross-cutting concerns.
  - `user-service/`: Handles registration, login, profiles, and social graph details.
  - `content-service/`: Manages pins, boards, uploads, and search features.
  - `collaboration-service/`: Covers shared boards, invitations, and related permissions.
  - `business-service/`: Supports business accounts, showcases, and sponsored pins.
- `frontend/`: Placeholder for the React or Angular single-page application.

Each service directory now contains a Spring Boot starter project with:
- A `pom.xml` aligned to Spring Boot 3.2.x, Resilience4j, Feign, Lombok, ModelMapper, and MySQL (where applicable).
- A minimal `PingController` you can hit through the gateway to confirm wiring.
- Default ports (gateway 8080, user 8081, content 8082, collaboration 8083, business 8084) and circuit-breaker defaults in `application.yml`.

The frontend directory includes a lightweight React + Vite scaffold with a landing layout that outlines the core product areas (register, login, create pins, boards, and search). Run `npm install` then `npm run dev` to preview it on port 5173.

### Implemented endpoints so far
- **User Service**: Registration (`/api/users/register`), login (`/api/users/login`) with circuit-breaker feedback on repeated failures.
- **Content Service**: Board creation/listing/detail endpoints (`/api/boards`) and pin creation/search/detail/delete endpoints (`/api/pins`) supporting draft/visibility flags and keyword metadata.

## Getting started
1. From each backend service directory, run `mvn spring-boot:run` to start the microservices locally (adjust database properties as needed before enabling JPA-backed features).
2. Stand up a Consul instance and MySQL database to satisfy service discovery and persistence needs.
3. From `frontend/`, run `npm install` followed by `npm run dev` to launch the SPA. Configure API URLs to pass through the gateway for consolidated routing and resilience.

## Notes
- The zip file at the repository root was intentionally ignored per the request.
- Add CI/CD, SonarQube scanning, and containerization as desired once services are implemented.
