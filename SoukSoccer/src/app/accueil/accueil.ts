import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../service/chat.service'; // adapte le chemin si besoin
import { ChatMessage } from '../chatbot/chat';
import { BABYFOOT_SYSTEM_PROMPT } from '../chatbot/system-prompt';

interface StatCard {
  icon: string;       // classe bootstrap-icons
  value: string;      // "1000+"
  label: string;      // "Étudiants connectés"
}

interface Feature {
  icon: string;
  title: string;
  desc: string;
}

interface LiveMatch {
  id: number;
  table: string;       // "Babyfoot #3"
  status: 'En cours' | 'Disponible';
  scoreLeft?: number;
  scoreRight?: number;
  since?: string;      // "12:34"
  tagColor: 'success' | 'warning' | 'danger' | 'secondary';
}

interface TopPlayer {
  rank: number;
  name: string;
  wins: number;
  points: number;
  avatar: string;
}

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [CommonModule, TranslatePipe, FormsModule],
  templateUrl: './accueil.html',
  styleUrls: ['./accueil.css']
})
export class Accueil {

  // ===== Hero CTA =====
  primaryCta() {
    // TODO: Naviguer vers /reservation ou /signup
    alert('TODO: Commencer maintenant');
  }

  // ===== Statistiques (en dur) =====
  stats = signal<StatCard[]>([
    // TODO Données à changer (GET /public/stats)
    { icon: 'bi-people', value: '1000+', label: 'HOME.STATS.CONNECTED' },
    { icon: 'bi-controller', value: '12', label: 'HOME.STATS.DISPONIBLE' },
    { icon: 'bi-trophy', value: '50+', label: 'HOME.STATS.TOURNAMENTS' },
    { icon: 'bi-clock-history', value: '24/7', label: 'HOME.STATS.HOURS_DISPO' },
  ]);

  // ===== Fonctionnalités (en dur) =====
  features = signal<Feature[]>([
    // TODO Données à changer (texte marketing)
    { icon: 'bi-calendar-check', title: 'HOME.FUNCTIONALITY.RESA.INT.TITLE', desc: 'HOME.FUNCTIONALITY.RESA.INT.DESC' },
    { icon: 'bi-activity',       title: 'HOME.FUNCTIONALITY.STAT.TITLE',        desc: 'HOME.FUNCTIONALITY.STAT.DESC' },
    { icon: 'bi-diagram-3',      title: 'HOME.FUNCTIONALITY.TOURNAMENTS.TITLE',     desc: 'HOME.FUNCTIONALITY.TOURNAMENTS.DESC' },
    { icon: 'bi-cpu',            title: 'HOME.FUNCTIONALITY.IOT.TITLE',             desc: 'HOME.FUNCTIONALITY.IOT.DESC' },
    { icon: 'bi-gear',           title: 'HOME.FUNCTIONALITY.GESTION.TITLE',          desc: 'HOME.FUNCTIONALITY.GESTION.DESC' },
    { icon: 'bi-phone',          title: 'HOME.FUNCTIONALITY.MOBILEAPP.TITLE',       desc: 'HOME.FUNCTIONALITY.MOBILEAPP.DESC' },
  ]);

  // ===== Activité en direct (en dur) =====
  liveMatches = signal<LiveMatch[]>([
    // TODO Données à changer (GET /public/live)
    { id: 3, table: 'Babyfoot #3', status: 'En cours', scoreLeft: 5, scoreRight: 3, since: '12:34', tagColor: 'success' },
    { id: 7, table: 'Babyfoot #7', status: 'En cours', scoreLeft: 2, scoreRight: 8, since: '08:45', tagColor: 'danger' },
    { id: 1, table: 'Babyfoot #1', status: 'Disponible', tagColor: 'secondary' },
  ]);

  topPlayers = signal<TopPlayer[]>([
    // TODO Données à changer (GET /public/leaderboard?limit=3)
    { rank: 1, name: 'Oskar M.', wins: 126, points: 2340, avatar: 'https://i.pravatar.cc/48?img=68' },
    { rank: 2, name: 'Maxime F.',     wins: 112, points: 2180, avatar: 'https://i.pravatar.cc/48?img=12' },
    { rank: 3, name: 'Raphaël Q.',    wins: 98,  points: 2050, avatar: 'https://i.pravatar.cc/48?img=22' },
  ]);

  // ====== CHATBOT (AJOUT) ======
  private chat = inject(ChatService);

  openChat = signal(false);
  draft = '';
  loading = signal(false);
  history = signal<ChatMessage[]>([
    { role: 'system', content: BABYFOOT_SYSTEM_PROMPT }
  ]);

  toggleChat() { this.openChat.set(!this.openChat()); }

  send() {
    const text = this.draft.trim();
    if (!text || this.loading()) return;

    this.history.update(h => [...h, { role: 'user', content: text }]);
    this.draft = '';
    this.loading.set(true);

    this.chat.send(this.history()).subscribe({
      next: reply => {
        this.history.update(h => [...h, { role: 'assistant', content: reply }]);
        this.loading.set(false);
      },
      error: () => {
        this.history.update(h => [...h, { role: 'assistant', content: "Oups… le coach est indisponible pour le moment." }]);
        this.loading.set(false);
      }
    });
  }
}
