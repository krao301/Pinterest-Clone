import apiService from './apiService';
import {
  LoginRequest,
  RegisterRequest,
  AuthResponse,
  ApiResponse,
  User,
} from '../types';

const axios = apiService.getAxiosInstance();

export const authService = {
  // Register new user
  register: async (data: RegisterRequest): Promise<ApiResponse<AuthResponse>> => {
    const url = process.env.REACT_APP_AUTH_BASE || 'http://localhost:9001/api/v1/auth';
    const response = await axios.post<ApiResponse<AuthResponse>>(`${url}/register`, data);
    // store tokens if provided
    if (response.data.success && response.data.data?.token) {
      localStorage.setItem('authToken', response.data.data.token);
    }
    if (response.data.success && response.data.data?.user) {
      localStorage.setItem('user', JSON.stringify(response.data.data.user));
    }
    return response.data;
  },

  // Login user
  login: async (data: LoginRequest): Promise<ApiResponse<AuthResponse>> => {
    const url = process.env.REACT_APP_AUTH_BASE || 'http://localhost:9001/api/v1/auth';
    const response = await axios.post<ApiResponse<AuthResponse>>(`${url}/login`, data);
    if (response.data.success && response.data.data.token) {
      localStorage.setItem('authToken', response.data.data.token);
    }
    if (response.data.success && response.data.data.user) {
      localStorage.setItem('user', JSON.stringify(response.data.data.user));
    }
    return response.data;
  },

  // Logout user
  logout: async (): Promise<void> => {
    try {
      const url = process.env.REACT_APP_AUTH_BASE || 'http://localhost:9001/api/v1/auth';
      await axios.post(`${url}/logout`, null, { withCredentials: true });
    } catch (e) {
      // ignore network errors
    }
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  },

  // Get current user
  getCurrentUser: (): User | null => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch {
        return null;
      }
    }
    return null;
  },

  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('authToken');
  },

  // Get user profile
  getUserProfile: async (userId: string): Promise<ApiResponse<User>> => {
    const response = await axios.get<ApiResponse<User>>(`/users/${userId}`);
    return response.data;
  },

  // Update user profile
  updateUserProfile: async (userId: string, data: Partial<User>): Promise<ApiResponse<User>> => {
    const response = await axios.put<ApiResponse<User>>(`/users/${userId}`, data);
    return response.data;
  },
};
