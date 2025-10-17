import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from './auth.service';

export interface Babyfoot {
  idBabyfoot: number;
  statutBabyfoot: 'free' | 'occupied' | 'maintenance';
  place: string;
}

@Injectable({ providedIn: 'root' })
export class BabyfootService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/api/babyfoots'; // adapte ton URL si besoin

  getAll(): Observable<Babyfoot[]> {
    return this.http.get<Babyfoot[]>(`${this.apiUrl}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  create(babyfoot: Partial<Babyfoot>): Observable<Babyfoot> {
    return this.http.post<Babyfoot>(`${this.apiUrl}`, babyfoot);
  }

  update(id: number, babyfoot: Partial<Babyfoot>): Observable<Babyfoot> {
    return this.http.put<Babyfoot>(`${this.apiUrl}/${id}`, babyfoot);
  }
}
