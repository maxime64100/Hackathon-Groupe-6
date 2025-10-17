import { Component, inject, OnInit } from '@angular/core';
import { LanguageService } from '../service/language.service';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../service/auth.service';
import { UserService, UserBabyfoot } from '../service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
  imports: [CommonModule, TranslatePipe],
})
export class Header implements OnInit {
  langService = inject(LanguageService);
  authService = inject(AuthService);
  userService = inject(UserService);
  router = inject(Router);
  role: string | null = null;

  isAuthenticated = false;
  userInfo: UserBabyfoot | null = null;

  ngOnInit() {
    console.log(this.userInfo?.role);
  }

  toggleLanguage() {
    this.langService.toggleLanguage();
  }

  get currentLang() {
    return this.langService.currentLang;
  }

  logout() {
    this.authService.logout();
    localStorage.removeItem('userId');
    this.router.navigate(['/login']);
  }
}
