# Pinterest Clone - Frontend

A full-featured Pinterest clone frontend built with React, TypeScript, HTML5, CSS3, and Bootstrap with OAuth authentication.

## Features

### 1. User Authentication (US 01 & US 02)
- **Registration Page** with comprehensive validation:
  - Email format validation (must end with .com, .org, or .in)
  - Username validation (lowercase letters, digits, special characters only)
  - Password validation (8-16 characters, must include uppercase, lowercase, digit, and special character)
  - Confirm password with real-time match checking
- **Login Page** with circuit breaker pattern:
  - 3 failed attempts within 30 seconds triggers circuit breaker
  - 60-second countdown timer displayed when circuit is open
  - Sponsored badge for sponsored pins
  # Pinterest-Clone (scaffold)

  This repository contains the frontend (React TypeScript) and a starter backend scaffold with two Spring Boot services (auth-service and content-service) and a Docker Compose file for two MySQL instances.

  What I scaffolded:
  - `frontend/` (existing React app)
  - `backend/` with two modules: `auth-service`, `content-service` (Maven multi-module root at `backend/pom.xml`).
  - `docker-compose.yml` starting two MySQL instances and Adminer.
  - `docs/` with architecture and implementation plan.

  Quick start (local development):

  1. Start the databases with Docker Compose:

  ```bash
  # from repository root
  - Home, Explore, Create links
  ```

  - Auth DB will be available at `localhost:3307` (mapped to container 3306).
  - Content DB will be available at `localhost:3308`.
  - Adminer UI at `http://localhost:8081` (user: `root`, password: `root`).
   - MinIO (S3-compatible) at `http://localhost:9000` (user: `minioadmin`, password: `minioadmin`).

  2. Build and run backend services (each in its module):

  ```bash
  # build
  cd backend
  mvn -q -DskipTests package

  # run auth-service (example, sets DB host/port to the docker-compose service)
  cd auth-service
  # set environment variables to point to docker mysql container
  export DB_HOST=localhost
  export DB_PORT=3307
  export DB_NAME=auth_db
  export DB_USER=root
  export DB_PASSWORD=root
  mvn spring-boot:run

  # in a second terminal run content-service similarly
  cd backend/content-service
  export DB_HOST=localhost
  export DB_PORT=3308
  export DB_NAME=content_db
  export DB_USER=root
  export DB_PASSWORD=root
  mvn spring-boot:run
  ```

  3. Frontend

  - The frontend is in `frontend/`. Install dependencies and run:

  ```bash
  cd frontend
  npm install
  npm start
  ```

  - Update `frontend` service endpoints (in `frontend/src/services/*.ts`) to point to the backend endpoints (e.g. `http://localhost:9001/api/v1/auth` and content service port 9002 when you run them).

  Notes & Next steps
  - The current backend is minimal: register/login are basic and return simple JSON (no JWT). Pins support basic CRUD.
  - Next steps: add JWT-based auth, Resilience4j circuit breakers, service discovery (Consul), S3-compatible storage, Redis caching, Kafka events, and extend APIs to match the QP.

  Run locally without Docker (recommended for quick dev)
  ---------------------------------------------------
  - The services default to an embedded H2 database so you can run them locally without Docker. You do not need to start Docker for the backend to work.
  - To run each service locally:

  ```bash
  # from repo root
  cd backend/auth-service
  mvn spring-boot:run

  # in another terminal
  cd backend/content-service
  mvn spring-boot:run
  ```

  - Frontend (same as above):
  ```bash
  cd frontend
  npm install
  npm start
  ```

  Uploads / file serving
  - When MinIO is not available the content service will write uploaded files to `./uploads/` and serve them as static resources at `http://localhost:9002/<filename>` (the service serves `./uploads/` by default). If you run with MinIO, uploaded object URLs will reference the MinIO endpoint.

  MinIO / Uploads
  - The Docker Compose includes MinIO. If you run `docker compose up -d` MinIO will be available at `http://localhost:9000`.
  - Content service reads the following env vars to upload files to MinIO:
    - `MINIO_ENDPOINT` (default `http://localhost:9000`)
    - `MINIO_ACCESS_KEY` (`minioadmin`)
    - `MINIO_SECRET_KEY` (`minioadmin`)
    - `MINIO_BUCKET` (default `pins`)
    - `MINIO_PUBLIC_URL` (optional public base for returned URLs)

  If you'd like, I can now:
  - Add JWT authentication and integrate the frontend login/register flows.
  - Add Resilience4j and the login-lockout timer behavior.
  - Add file uploads/MinIO integration for pins
  - Extend content APIs (boards, privacy, search)

  Which feature should I implement next (pick one to start):
  - Add JWT auth + integrate frontend
  - Implement Resilience4j login lockout and timer behavior
  - Add file uploads/MinIO integration for pins
  - Extend content APIs (boards, privacy, search)
  - Search bar
  - Notifications icon
  - Messages icon
  - User profile dropdown with:
    - Profile link
    - My Boards
    - Saved items
    - Invitations
    - Settings
    - Business Account (if applicable)
    - Logout option (US 10)

## Technology Stack

- **React 18** - UI framework
- **TypeScript** - Type safety
- **React Router v6** - Client-side routing
- **Bootstrap 5** - UI components and responsive design
- **React Bootstrap** - Bootstrap components for React
- **Axios** - HTTP client for API calls
- **Bootstrap Icons** - Icon library
- **CSS3** - Custom styling

## Project Structure

```
frontend/
├── public/
│   ├── index.html
│   └── manifest.json
├── src/
│   ├── components/
│   │   ├── Navigation/
│   │   │   ├── Navigation.tsx
│   │   │   └── Navigation.css
│   │   ├── PinCard/
│   │   │   ├── PinCard.tsx
│   │   │   └── PinCard.css
│   │   ├── BoardCard/
│   │   │   ├── BoardCard.tsx
│   │   │   └── BoardCard.css
│   │   └── UserCard/
│   │       ├── UserCard.tsx
│   │       └── UserCard.css
│   ├── pages/
│   │   ├── Auth/
│   │   │   ├── Login.tsx
│   │   │   ├── Register.tsx
│   │   │   └── Auth.css
│   │   ├── Dashboard/
│   │   │   ├── Dashboard.tsx
│   │   │   └── Dashboard.css
│   │   ├── Pin/
│   │   │   ├── CreatePin.tsx
│   │   │   ├── PinDetail.tsx
│   │   │   └── Pin.css
│   │   ├── Board/
│   │   │   ├── BoardDetail.tsx
│   │   │   └── Board.css
│   │   ├── Search/
│   │   │   ├── Search.tsx
│   │   │   └── Search.css
│   │   ├── Profile/
│   │   │   ├── Profile.tsx
│   │   │   └── Profile.css
│   │   ├── Social/
│   │   │   ├── Invitations.tsx
│   │   │   └── Invitations.css
│   │   └── Business/
│   │       ├── BusinessDashboard.tsx
│   │       └── BusinessDashboard.css
│   ├── services/
│   │   ├── apiService.ts
│   │   ├── authService.ts
│   │   ├── pinService.ts
│   │   ├── boardService.ts
│   │   ├── searchService.ts
│   │   ├── socialService.ts
│   │   └── businessService.ts
│   ├── types/
│   │   └── index.ts
│   ├── App.tsx
│   ├── App.css
│   ├── index.tsx
│   └── index.css
├── .env
├── .gitignore
├── package.json
├── tsconfig.json
└── README.md
```

## Setup Instructions

### Prerequisites
- Node.js 16+ installed
- npm or yarn package manager

### Installation

1. Navigate to the frontend directory:
```bash
cd "Pinterest Clone/frontend"
```

2. Install dependencies:
```bash
npm install
```

3. Configure environment variables:
Edit the `.env` file in the frontend directory:
```env
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_OAUTH_CLIENT_ID=your-client-id
REACT_APP_OAUTH_REDIRECT_URI=http://localhost:3000/callback
REACT_APP_OAUTH_AUTHORIZATION_ENDPOINT=http://localhost:8080/oauth/authorize
REACT_APP_OAUTH_TOKEN_ENDPOINT=http://localhost:8080/oauth/token
```

4. Start the development server:
```bash
npm start
```

The application will open in your browser at `http://localhost:3000`

### Build for Production

```bash
npm run build
```

This creates an optimized production build in the `build` folder.

## API Integration

The frontend communicates with the backend microservices through the following services:

- **Authentication Service** - User registration, login, profile management
- **Pin Service** - Create, read, update, delete pins
- **Board Service** - Manage boards and board collaborations
- **Search Service** - Search pins, boards, and users
- **Social Service** - Follow/unfollow, invitations, followers/following
- **Business Service** - Business profiles, campaigns, sponsored pins

All API calls include:
- JWT token authentication (stored in localStorage)
- Automatic token injection via Axios interceptors
- Error handling with user-friendly messages
- Automatic redirect to login on 401 errors

## Key Features Implementation

### Circuit Breaker Pattern (Login)
- Tracks failed login attempts
- Opens circuit after 3 failures within 30 seconds
- Displays countdown timer for 60 seconds
- Stores state in localStorage for persistence
- Auto-resets after timeout

### Validation
- Email: Must match pattern with .com/.org/.in domains
- Username: Lowercase letters, digits, special characters only
- Password: 8-16 chars, must include uppercase, lowercase, digit, special character
- Real-time validation feedback

### Responsive Design
- Mobile-first approach
- Breakpoints for mobile, tablet, and desktop
- Masonry layout for pins (1-5 columns based on screen size)
- Touch-friendly buttons and interactions
- Bootstrap grid system

### Image Lazy Loading
- Images load as they enter viewport
- Reduces initial page load time
- Smooth user experience
- Placeholder images for failed loads

### OAuth Integration
- Ready for OAuth 2.0 implementation
- Environment variables for OAuth configuration
- Protected routes for authenticated users
- Public routes redirect to dashboard if logged in

## User Stories Coverage

✅ **US 01: User Registration** - Complete with validation  
✅ **US 02: Login** - Complete with circuit breaker  
✅ **US 03: Create Pin** - Complete with draft and preview  
✅ **US 04: Retrieval of Pins and Boards** - Complete with detailed views  
✅ **US 05: Search Pins and Boards** - Complete with filters and suggestions  
✅ **US 06: Follow/Unfollow** - Complete with follower lists  
✅ **US 07: View Invitations** - Complete with accept/decline  
✅ **US 08: Business Profiles** - Complete with dashboard  
✅ **US 09: Advertising Campaigns** - Complete with sponsored pins  
✅ **US 10: Log Out** - Complete with session cleanup  

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

This is a project implementation based on the Pinterest-QP.txt requirements document.

## License

This is an educational project.
