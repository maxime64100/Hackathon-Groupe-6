import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  onSubmit() {
    alert('Compte créé avec succès (stub)');
  }
}
