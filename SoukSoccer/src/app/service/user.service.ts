import {Injectable, inject, Inject, PLATFORM_ID} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import {isPlatformBrowser} from '@angular/common';
import {environment} from '../../environments/environment';

export interface UserBabyfoot {
  idUser: number;
  mail: string;
  name: string;
  surname: string;
  role: string;
  passwordUser: string;
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
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api';
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  /** ✅ Ajoute automatiquement le token dans le header (uniquement côté navigateur) */
  private getAuthHeaders(): HttpHeaders {
    let token = '';

    if (this.isBrowser) {
      token = localStorage.getItem('token') ?? '';
    }

    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  getUsers(page = 0, size = 10): Observable<PaginatedUsers> {
    const headers = this.getAuthHeaders();
    return this.http.get<PaginatedUsers>(
      `${this.apiUrl}/users?page=${page}&size=${size}`,
      { headers }
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${id}`);
  }

  getUserById(id: number): Observable<UserBabyfoot> {
    const headers = this.getAuthHeaders();
    return this.http.get<UserBabyfoot>(`${this.apiUrl}/users/${id}`, { headers });
  }

  updateUser(id: number, user: Partial<UserBabyfoot>): Observable<UserBabyfoot> {
    const headers = this.getAuthHeaders();
    return this.http.put<UserBabyfoot>(`${this.apiUrl}/users/${id}`, user, { headers });
  }

  createUser(user: Partial<UserBabyfoot>): Observable<string> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/auth/register`, user, {
      headers,
      responseType: 'text'
    });
  }

  getStats(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.apiUrl}/stats`, { headers });
  }

}

