import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserRef {
  idUser: number;
}
export interface BabyfootRef {
  idBabyfoot: number;
}

export interface Reservation {
  idBooking: number;
  statutBooking: string;
  dateBooking: string;
  user: UserRef;
  babyfoot: BabyfootRef;
}

export interface CreateBookingDto {
  statutBooking: string;
  dateBooking: string;
  user: UserRef;
  babyfoot: BabyfootRef;
}
export type UpdateBookingDto = CreateBookingDto;

@Injectable({ providedIn: 'root' })
export class BookingService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/bookings';

  getAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.baseUrl);
  }

  create(payload: CreateBookingDto): Observable<Reservation> {
    return this.http.post<Reservation>(this.baseUrl, payload);
  }

  update(id: number, payload: UpdateBookingDto): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.baseUrl}/${id}`);
  }
}
