import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { BookingService, CreateBookingDto } from '../service/reservation.service';
import { AuthService } from '../service/auth.service';

type Role = 'user' | 'admin';

interface Day {
  label: string;
  date: Date;
  dayNum: number;
  isSelected: boolean;
}
interface TimeSlot {
  label: string;
  durationMin: number;
  disabled?: boolean;
  occupied?: boolean;
}
interface TableStatus {
  color: 'green' | 'red' | 'gray';
  text: string;
}
interface BabyfootTable {
  id: number;
  name: string;
  zone: string;
  status: TableStatus;
  disabled?: boolean;
}
interface QuickStats {
  available: number;
  total: number;
  avgWaitMin: number;
  gamesToday: number;
}
interface MyReservationItem {
  when: 'Aujourd’hui' | 'Demain' | 'Autre';
  tableName: string;
  time: string;
  status: 'Actif' | 'Planifié';
}
interface PopularHour {
  range: string;
  level: 'high' | 'medium' | 'low';
}
interface Player {
  id: number;
  name: string;
  avatarUrl: string;
}

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './reservation.html',
  styleUrls: ['./reservation.css']
})
export class Reservation {
  private auth = inject(AuthService);
  private bookingService = inject(BookingService);

  days = signal<Day[]>(this.buildWeek(new Date()));
  selectedDay = signal<Day>(this.days()[2] ?? this.days()[0]);

  timeSlots = signal<TimeSlot[]>([
    { label: '08:00', durationMin: 30 },
    { label: '08:30', durationMin: 30 },
    { label: '09:00', durationMin: 30 },
    { label: '09:30', durationMin: 30 },
    { label: '10:00', durationMin: 30 },
    { label: '10:30', durationMin: 30, disabled: true, occupied: true },
    { label: '11:00', durationMin: 30 },
    { label: '11:30', durationMin: 30 },
  ]);
  selectedSlot = signal<TimeSlot | null>(this.timeSlots()[2]);

  role: Role = 'user';

  tables = signal<BabyfootTable[]>([
    { id: 1, name: 'Babyfoot #1', zone: 'Zone principale', status: { color: 'green', text: 'OK' } },
    { id: 3, name: 'Babyfoot #3', zone: 'Zone détente',   status: { color: 'green', text: 'OK' } },
    { id: 5, name: 'Babyfoot #5', zone: 'Zone calme',      status: { color: 'green', text: 'OK' } },
    { id: 7, name: 'Babyfoot #7', zone: 'Maintenance',     status: { color: 'gray',  text: 'Maintenance' }, disabled: true },
  ]);
  selectedTable = signal<BabyfootTable | null>(this.tables()[1]);

  players = signal<Player[]>([
    { id: 1, name: 'Vous', avatarUrl: 'https://i.pravatar.cc/48?img=5' },
  ]);
  maxPlayers = 4;
  canAddMore = computed(() => this.players().length < this.maxPlayers);

  quickStats = signal<QuickStats>({ available: 8, total: 12, avgWaitMin: 5, gamesToday: 47 });

  myReservations = signal<MyReservationItem[]>([
    { when: 'Aujourd’hui', tableName: 'Babyfoot #3', time: '14:30', status: 'Actif' },
    { when: 'Demain',      tableName: 'Babyfoot #1', time: '10:00', status: 'Planifié' },
  ]);

  popularHours = signal<PopularHour[]>([
    { range: '12:00 - 14:00', level: 'high' },
    { range: '18:00 - 20:00', level: 'medium' },
    { range: '08:00 - 10:00', level: 'low' },
  ]);

  // ======= ACTIONS =======
  selectDay(day: Day) {
    this.days.update(list => list.map(d => ({ ...d, isSelected: d === day })));
    this.selectedDay.set(day);
  }

  selectSlot(slot: TimeSlot) {
    if (slot.disabled) return;
    this.selectedSlot.set(slot);
  }

  selectTable(t: BabyfootTable) {
    if (t.disabled) return;
    this.selectedTable.set(t);
  }

  addPlayer() {
    if (!this.canAddMore()) return;
    const id = Math.floor(Math.random() * 100000);
    this.players.update(arr => [...arr, { id, name: 'Invité', avatarUrl: `https://i.pravatar.cc/48?u=${id}` }]);
  }

  removePlayer(p: Player) {
    if (p.name === 'Vous') return;
    this.players.update(arr => arr.filter(x => x.id !== p.id));
  }

  confirmReservation() {
    if (!this.selectedDay() || !this.selectedSlot() || !this.selectedTable()) {
      alert('Sélectionne une date, un créneau et une table 👍');
      return;
    }


    const currentUserId = this.auth.getUserId();  // <-- récupère depuis le JWT / localStorage
    if (!currentUserId) {
      alert('Tu dois être connecté pour réserver.');
      return;
    }    const babyfootId = this.selectedTable()!.id;

    const payload: CreateBookingDto = {
      statutBooking: 'ACCEPTEE',
      dateBooking: this.toLocalIso(this.selectedDay().date, this.selectedSlot()!.label),
      user: { idUser: currentUserId },
      babyfoot: { idBabyfoot: babyfootId }
    };

    this.bookingService.create(payload).subscribe({
      next: (created) => {
        alert(`✅ Réservation créée (#${created.idBooking}) le ${this.formatDate(this.selectedDay().date)} à ${this.selectedSlot()!.label}`);
      },
      error: (err) => {
        // Log complet pour debug
        console.error('Booking create error =>', err);
        const backendMsg = err?.error?.error;         // ton contrôleur renvoie { error: "..." }
        const httpMsg    = err?.message || 'HTTP error';
        const finalMsg   = backendMsg || httpMsg || 'Une erreur est survenue';
        alert(`❌ ${finalMsg}`);
      }
    });
  }

  /** Construit "YYYY-MM-DDTHH:mm:00" (sans timezone) — idéal pour Spring LocalDateTime */
  private toLocalIso(date: Date, timeLabel: string): string {
    const [h, m] = timeLabel.split(':').map(Number);
    const d = new Date(date);
    d.setHours(h, m, 0, 0);

    const pad = (n: number) => String(n).padStart(2, '0');
    const y = d.getFullYear();
    const mo = pad(d.getMonth() + 1);
    const da = pad(d.getDate());
    const ho = pad(d.getHours());
    const mi = pad(d.getMinutes());
    return `${y}-${mo}-${da}T${ho}:${mi}:00`;
  }

  // ======= HELPERS =======
  private buildWeek(reference: Date): Day[] {
    const base = new Date(reference);
    const dow = (base.getDay() + 6) % 7; // 0=lundi
    base.setDate(base.getDate() - dow);
    const labels = ['LUN', 'MAR', 'MER', 'JEU', 'VEN', 'SAM', 'DIM'];
    return Array.from({ length: 7 }).map((_, i) => {
      const d = new Date(base);
      d.setDate(base.getDate() + i);
      return {
        label: labels[i],
        date: d,
        dayNum: d.getDate(),
        isSelected: labels[i] === 'MER',
      };
    });
  }

  formatDate(date: Date) {
    return date.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit' });
  }
}
