import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ApiBabyfoot {
  idBabyfoot: number;
  place: string;
  status?: string;
}

@Injectable({ providedIn: 'root' })
export class BabyfootService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/babyfoots';

  getAll(): Observable<ApiBabyfoot[]> {
    return this.http.get<ApiBabyfoot[]>(this.baseUrl);
  }
}
