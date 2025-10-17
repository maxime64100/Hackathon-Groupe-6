import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './header/header';
import {LanguageService} from './service/language.service';
import {Footer} from './footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer],
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.css'
})
export class App {
  // ✅ juste une référence
  langService = inject(LanguageService);
}
