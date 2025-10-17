import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import {UserBabyfoot} from '../service/user.service';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  userInfo: UserBabyfoot | null = null;
  constructor(private router: Router) {}

  canActivate(): boolean {
    if (this.userInfo?.role) {
      return true;
    }

    this.router.navigate(['/']); // redirige vers l'accueil ou le dashboard
    return false;
  }
}
