import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  private http = inject(HttpClient);
  private router = inject(Router);

  submitting = false;
  serverMessage: string | null = null;
  serverError: string | null = null;

  onSubmit(form: NgForm) {
    if (form.invalid) return;

    const { name, surname, email, password, confirmPassword } = form.value;

    if (password !== confirmPassword) {
      this.serverError = 'Les mots de passe ne correspondent pas.';
      this.serverMessage = null;
      return;
    }

    const payload = {
      name,
      surname,
      mail: email,
      passwordUser: password
    };

    this.submitting = true;
    this.serverMessage = null;
    this.serverError = null;

    this.http.post('http://localhost:8080/api/auth/register', payload, { responseType: 'text' }).subscribe({
      next: (res) => {
        this.serverMessage = res;
        this.submitting = false;
        setTimeout(() => this.router.navigate(['/login']), 800);
      },
      error: (err) => {
        this.serverError = err.error || 'Erreur lors de l’inscription.';
        this.submitting = false;
      }
    });
  }
}
