import {Component, inject} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Header} from './header/header';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header],
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.css'
})
export class App {
  private translate = inject(TranslateService);

  constructor() {
    this.translate.addLangs(['fr', 'en']);
    this.translate.setFallbackLang('fr');
    this.translate.use('fr');
  }
}
