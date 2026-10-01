export interface RegisterRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
  }
  
  export interface LoginRequest {
    email: string;
    password: string;
  }
  
  export interface AuthResponse {
    token: string;
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    role: 'USER' | 'ADMIN';
  }