import {Component, inject} from '@angular/core';
import {CommonModule, DOCUMENT} from '@angular/common';
import {TranslateDirective, TranslatePipe, TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class Header {
  private translate = inject(TranslateService);
  private document = inject(DOCUMENT);
  currentLang = 'fr';

  constructor() {
    this.translate.addLangs(['fr', 'en']);

    // ✅ on force par défaut le français
    const defaultLang = 'fr';

    const isBrowser = typeof window !== 'undefined' && typeof localStorage !== 'undefined';
    let savedLang: string | null = null;

    if (isBrowser) {
      savedLang = localStorage.getItem('lang');
    }

    // ✅ Si rien n'est sauvegardé, on utilise le français
    this.currentLang = savedLang || defaultLang;

    this.setLanguage(this.currentLang);
  }

  // ✅ Méthode toggle : inverse entre fr/en
  toggleLanguage() {
    const newLang = this.currentLang === 'fr' ? 'en' : 'fr';
    this.setLanguage(newLang);
  }

  private setLanguage(lang: string) {
    this.currentLang = lang;
    this.translate.use(lang);

    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      localStorage.setItem('lang', lang);
    }

    this.document.documentElement.lang = lang;

    // ✅ Met à jour le titre traduit (facultatif)
    this.translate.get('HOME.TITLE').subscribe((title) => {
      this.document.title = title;
    });
  }
}
