import { Component, inject } from '@angular/core';
import {LanguageService} from '../../shared/service/language.service';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.html',
  imports: [
    TranslatePipe
  ],
  styleUrl: './header.css'
})
export class Header {
  langService = inject(LanguageService);

  toggleLanguage() {
    this.langService.toggleLanguage();
  }

  get currentLang() {
    return this.langService.currentLang;
  }
}
