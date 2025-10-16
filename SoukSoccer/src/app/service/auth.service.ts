import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { isPlatformBrowser } from '@angular/common';

export interface DecodedToken extends JwtPayload {
  sub?: string;
  name?: string;
  surname?: string;
  mail?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isBrowser: boolean;

  private authState = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.authState.asObservable();

  private userInfoSubject = new BehaviorSubject<DecodedToken | null>(null);
  userInfo$ = this.userInfoSubject.asObservable();

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);

    if (this.isBrowser) {
      const hasToken = this.hasValidToken();
      this.authState.next(hasToken);
      this.userInfoSubject.next(this.getUserInfo());
    }
  }

  /** ✅ Vérifie s’il y a un token valide */
  private hasValidToken(): boolean {
    if (!this.isBrowser) return false;

    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      if (decoded.exp && Date.now() / 1000 > decoded.exp) {
        this.logout();
        return false;
      }
      return true;
    } catch {
      return false;
    }
  }

  /** ✅ Décode le token pour récupérer les infos utilisateur */
  getUserInfo(): DecodedToken | null {
    if (!this.isBrowser) return null;

    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      return jwtDecode<DecodedToken>(token);
    } catch {
      return null;
    }
  }

  /** ✅ Sauvegarde le token et met à jour le state */
  saveToken(token: string, userId: string) {
    if (!this.isBrowser) return;

    localStorage.setItem('token', token);
    localStorage.setItem('userId', userId);

    this.authState.next(true);
    this.userInfoSubject.next(this.getUserInfo());
  }

  /** ✅ Supprime le token et met à jour le state */
  logout() {
    if (!this.isBrowser) return;

    localStorage.removeItem('token');
    this.authState.next(false);
    this.userInfoSubject.next(null);
  }
}
