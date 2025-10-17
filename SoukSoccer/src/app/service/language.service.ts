import { Injectable, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root' // ✅ rend ce service dispo partout sans import
})
export class LanguageService {
  private translate = inject(TranslateService);
  private document = inject(DOCUMENT);
  currentLang = 'fr';

  constructor() {
    this.translate.addLangs(['fr', 'en']);
    this.translate.setDefaultLang('fr');
    this.translate.use('fr');

    const isBrowser = typeof window !== 'undefined' && typeof localStorage !== 'undefined';
    const savedLang = isBrowser ? localStorage.getItem('lang') : null;
    this.currentLang = savedLang || 'fr';

    this.setLanguage(this.currentLang);
  }

  toggleLanguage() {
    const newLang = this.currentLang === 'fr' ? 'en' : 'fr';
    this.setLanguage(newLang);
  }

  setLanguage(lang: string) {
    this.currentLang = lang;
    this.translate.use(lang);

    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      localStorage.setItem('lang', lang);
    }

    this.document.documentElement.lang = lang;

    // Optionnel : mise à jour du titre
    this.translate.get('HOME.TITLE').subscribe(title => {
      this.document.title = title;
    });
  }
}
