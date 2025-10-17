import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ChatMessage, ChatRequest, ChatResponse } from '../chatbot/chat';

@Injectable({ providedIn: 'root' })
export class ChatService {
  private http = inject(HttpClient);
  private baseUrl = '/api/chat';

  send(messages: ChatMessage[], options?: Partial<ChatRequest>): Observable<string> {
    const payload: ChatRequest = {
      messages,
      model: options?.model ?? 'gpt-4o-mini',
      temperature: options?.temperature ?? 0.7,
      max_tokens: options?.max_tokens ?? 500
    };
    return this.http.post<ChatResponse>(this.baseUrl, payload).pipe(
      map(r => r.reply)
    );
  }
}
