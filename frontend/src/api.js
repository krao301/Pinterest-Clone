const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  });

  const contentType = response.headers.get('content-type') || '';
  const payload = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    const error = new Error(payload?.error || 'Request failed');
    error.status = response.status;
    error.payload = payload;
    throw error;
  }
  return payload;
}

export async function registerUser(body) {
  return request('/api/users/register', { method: 'POST', body: JSON.stringify(body) });
}

export async function loginUser(body) {
  return request('/api/users/login', { method: 'POST', body: JSON.stringify(body) });
}

export async function fetchPins(params = {}) {
  const search = new URLSearchParams(params);
  const query = search.toString();
  return request(`/api/pins${query ? `?${query}` : ''}`);
}

export async function createPin(body) {
  return request('/api/pins', { method: 'POST', body: JSON.stringify(body) });
}

export async function fetchBoards() {
  return request('/api/boards');
}

export async function createBoard(body) {
  return request('/api/boards', { method: 'POST', body: JSON.stringify(body) });
}

export async function listFollowers(userId) {
  if (!userId) return [];
  return request(`/api/users/${userId}/followers`);
}

export async function listFollowing(userId) {
  if (!userId) return [];
  return request(`/api/users/${userId}/following`);
}

export async function followUser(followerId, followedId) {
  return request(`/api/users/${followerId}/follow/${followedId}`, { method: 'POST' });
}

export async function unfollowUser(followerId, followedId) {
  return request(`/api/users/${followerId}/follow/${followedId}`, { method: 'DELETE' });
}

export async function fetchInvitations(inviteeId, status) {
  if (!inviteeId) return [];
  const params = new URLSearchParams({ inviteeId });
  if (status) params.set('status', status);
  return request(`/api/invitations?${params.toString()}`);
}

export async function sendInvitation(body) {
  return request('/api/invitations', { method: 'POST', body: JSON.stringify(body) });
}

export async function updateInvitation(id, action) {
  return request(`/api/invitations/${id}/${action}`, { method: 'POST' });
}

export async function fetchBusinessProfiles(params = {}) {
  const search = new URLSearchParams(params);
  const query = search.toString();
  return request(`/api/business/profiles${query ? `?${query}` : ''}`);
}

export async function fetchShowcases(params = {}) {
  const search = new URLSearchParams(params);
  const query = search.toString();
  return request(`/api/business/showcases${query ? `?${query}` : ''}`);
}

export async function fetchSponsoredPins(params = {}) {
  const search = new URLSearchParams(params);
  const query = search.toString();
  return request(`/api/business/sponsored${query ? `?${query}` : ''}`);
}

export function getApiBase() {
  return API_BASE;
}

