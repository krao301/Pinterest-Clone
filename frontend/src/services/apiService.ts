import axios, { AxiosInstance, AxiosError } from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:9002/api/v1';

class ApiService {
  private axiosInstance: AxiosInstance;

  constructor() {
    this.axiosInstance = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
        withCredentials: true,
    });

    // Request interceptor
    this.axiosInstance.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Response interceptor
    this.axiosInstance.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const originalRequest: any = error.config;
        // if unauthorized, try refresh flow once (credentialed cookie-based refresh)
        if (error.response?.status === 401 && !originalRequest?._retry) {
          originalRequest._retry = true;
          const authBase = process.env.REACT_APP_AUTH_BASE || 'http://localhost:9001/api/v1/auth';
          try {
            const resp = await fetch(`${authBase}/refresh`, {
              method: 'POST',
              credentials: 'include',
            });
            if (resp.ok) {
              const body = await resp.json();
              const newToken = body.data?.token || body.token || body.accessToken;
              if (newToken) {
                localStorage.setItem('authToken', newToken);
                // update header and retry original request
                originalRequest.headers = originalRequest.headers || {};
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
                return this.axiosInstance(originalRequest);
              }
            }
          } catch (e) {
            // ignore and fall through to logout
          }
          // refresh failed — clear auth and redirect to login
          localStorage.removeItem('authToken');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  public getAxiosInstance(): AxiosInstance {
    return this.axiosInstance;
  }

  public handleError(error: any): string {
    if (error.response?.data?.message) {
      return error.response.data.message;
    }
    if (error.message) {
      return error.message;
    }
    return 'An unexpected error occurred';
  }
}

export default new ApiService();
