import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {AuthService} from '../service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);

  submitting = false;
  serverMessage: string | null = null;
  serverError: string | null = null;

  onSubmit(form: NgForm) {
    if (form.invalid) return;

    const { email, password } = form.value;

    const payload = {
      mail: email,        // ✅ correspond exactement à ton backend
      password: password  // ✅ correspond à AuthRequest.password
    };

    this.submitting = true;
    this.serverMessage = null;
    this.serverError = null;

    this.http.post<{ token: string, userId: string }>('http://localhost:8080/api/auth/login', payload)
      .subscribe({
        next: (res) => {
          // ✅ Stocke le JWT dans le localStorage
          this.authService.saveToken(res.token, res.userId);
          this.serverMessage = 'Connexion réussie ✅';
          this.submitting = false;

          // redirection après succès
          setTimeout(() => this.router.navigate(['/']), 800);
        },
        error: (err) => {
          this.serverError = err.error?.message || 'Identifiants incorrects.';
          this.submitting = false;
        }
      });
  }
}
