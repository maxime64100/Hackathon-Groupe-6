import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./login.css']
})
export class LoginComponent {
  onSubmit() {
    alert('Connexion réussie (stub)');
  }
}
