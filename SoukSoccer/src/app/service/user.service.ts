import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserBabyfoot {
  idUser: number;
  name: string;
  surname: string;
  mail: string;
  profileImageUrl: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/users'; // adapte selon ton mapping

  getUserById(id: number): Observable<UserBabyfoot> {
    return this.http.get<UserBabyfoot>(`${this.baseUrl}/${id}`);
  }
}
