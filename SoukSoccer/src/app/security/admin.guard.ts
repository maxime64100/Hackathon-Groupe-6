import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { UserService } from '../service/user.service';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) {}

  async canActivate(): Promise<boolean> {
    const userId = this.authService.getUserId();

    if (!userId) {
      this.router.navigate(['/login']);
      return false;
    }

    try {
      const user = await firstValueFrom(this.userService.getUserById(userId));

      if (user.role === 'ADMIN') {
        return true; // ✅ accès autorisé
      } else {
        alert('Accès refusé : réservé aux administrateurs 🚫');
        this.router.navigate(['/']);
        return false;
      }

    } catch (err) {
      console.error('Erreur lors du contrôle du rôle', err);
      this.router.navigate(['/login']);
      return false;
    }
  }
}
