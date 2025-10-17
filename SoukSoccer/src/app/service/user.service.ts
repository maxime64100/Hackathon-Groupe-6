import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service'; // <== importe ton AuthService

export interface UserBabyfoot {
  idUser: number;
  mail: string;
  name: string;
  surname: string;
  profileImageUrl?: string;
}

export interface PaginatedUsers {
  users: UserBabyfoot[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private authService = inject(AuthService); // <== injecte AuthService
  private apiUrl = 'http://localhost:8080/api/users';

  /** ✅ Ajoute automatiquement le token dans le header */
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  getUsers(page = 0, size = 10): Observable<PaginatedUsers> {
    const headers = this.getAuthHeaders();
    return this.http.get<PaginatedUsers>(
      `${this.apiUrl}?page=${page}&size=${size}`,
      { headers }
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getUserById(id: number): Observable<UserBabyfoot> {
    const headers = this.getAuthHeaders();
    return this.http.get<UserBabyfoot>(`${this.apiUrl}/${id}`, { headers });
  }

  updateUser(id: number, user: Partial<UserBabyfoot>): Observable<UserBabyfoot> {
    const headers = this.getAuthHeaders();
    return this.http.put<UserBabyfoot>(`${this.apiUrl}/${id}`, user, { headers });
  }
}
