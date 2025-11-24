# Frontend (Pinterest-style SPA)

This frontend is a Vite-powered React single-page experience styled to mirror the Pinterest home feed: sticky top navigation with search, pill filters, and a masonry grid of pins with hover save controls.

Implemented UX highlights:
- Registration/login card with validation that enforces email/username rules, password strength, confirm-password messaging, and Resilience4j-inspired login throttling with a countdown indicator.
- Pin and board creation forms with visibility controls, topic selection, and draft defaults that immediately surface saved items in the feed and boards list.
- Search across pins and boards, filter chips, save toggles, and private-pin visibility that unlocks once saved.
- Followers/following management, invitations with accept/decline flows, business profile exploration with follow actions, and sponsored pin placements clearly labeled.
- Logout control from the avatar pill to clear the session and saved pins.

Setup:
1. `cd frontend && npm install`
2. `npm run dev` for local development or `npm run build` for a production bundle.
3. Replace the in-memory data structures in `src/App.jsx` with real API calls routed through the gateway service, handling OAuth/Spring Security tokens as required by the backend.
