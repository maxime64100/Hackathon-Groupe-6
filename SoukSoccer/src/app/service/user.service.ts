import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserBabyfoot {
  idUser: number;
  mail: string;
  name: string;
  surname: string;
  profileImageUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/users'; // adapte si besoin

  getUserById(id: number): Observable<UserBabyfoot> {
    return this.http.get<UserBabyfoot>(`${this.apiUrl}/${id}`);
  }

  updateUser(id: number, user: Partial<UserBabyfoot>): Observable<UserBabyfoot> {
    return this.http.put<UserBabyfoot>(`${this.apiUrl}/${id}`, user);
  }
}
