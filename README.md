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

Each service directory currently includes a `README.md` that outlines the responsibilities, expected Spring Boot stack, and recommended configuration (Consul, Resilience4j, Swagger, ModelMapper, Lombok, MySQL). No code has been generated yet, keeping the repository ready for an implementation that can cleanly map to your deployment preferences.

## Getting started
1. Initialize each backend service with your preferred build tool (Maven or Gradle) and apply the notes in the service-specific README files.
2. Stand up a Consul instance and MySQL database to satisfy service discovery and persistence needs.
3. Scaffold the frontend SPA within `frontend/`, wiring it to the gateway for API access.

## Notes
- The zip file at the repository root was intentionally ignored per the request.
- Add CI/CD, SonarQube scanning, and containerization as desired once services are implemented.
