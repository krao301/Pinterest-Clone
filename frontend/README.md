# Frontend (Pinterest-style SPA)

This frontend is a Vite-powered React single-page experience styled to mirror the Pinterest home feed: sticky top navigation with search, pill filters, and a masonry grid of pins with hover save controls.

Responsibilities:
- Implement registration/login flows, content creation, pin/board browsing, search, followers/following, invitations, business profiles, and logout.
- Integrate with the gateway service for all API calls, handling OAuth/Spring Security tokens where applicable.
- Provide responsive layouts with lazy loading for media, optimistic UI updates, and masonry-style discovery grids.

Setup:
1. `cd frontend && npm install`
2. `npm run dev` for local development or `npm run build` for a production bundle.
3. Extend `src/App.jsx` with routed views for authentication, pins/boards, search, social graph, invitations, and business experiences.
