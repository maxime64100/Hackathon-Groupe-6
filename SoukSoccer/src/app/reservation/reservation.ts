import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { AuthService } from '../service/auth.service';
import { BookingService, CreateBookingDto } from '../service/reservation.service';
import { BabyfootService, ApiBabyfoot } from '../babyfoot/babyfoot.service';

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
export class Reservation implements OnInit {
  private auth = inject(AuthService);
  private bookingService = inject(BookingService);
  private babyfootService = inject(BabyfootService);

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
  selectedSlot = signal<TimeSlot | null>(this.timeSlots()[2]); // 09:00

  babyfoots = signal<ApiBabyfoot[]>([]);
  selectedBabyfoot = signal<ApiBabyfoot | null>(null);

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

  // ======= Lifecycle =======
  ngOnInit(): void {
    this.loadBabyfoots();
  }

  private loadBabyfoots() {
    this.babyfootService.getAll().subscribe({
      next: (list) => {
        this.babyfoots.set(list);
        this.selectedBabyfoot.set(list[0] ?? null);
      },
      error: (err) => {
        console.error('Erreur chargement babyfoots', err);
      }
    });
  }

  selectDay(day: Day) {
    this.days.update(list => list.map(d => ({ ...d, isSelected: d === day })));
    this.selectedDay.set(day);
  }

  selectSlot(slot: TimeSlot) {
    if (slot.disabled) return;
    this.selectedSlot.set(slot);
  }

  selectBabyfoot(b: ApiBabyfoot) {
    this.selectedBabyfoot.set(b);
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
    const chosenDay = this.selectedDay();
    const chosenSlot = this.selectedSlot();
    const chosenBaby = this.selectedBabyfoot();

    if (!chosenDay || !chosenSlot || !chosenBaby) {
      alert('Sélectionne une date, un créneau et une table 👍');
      return;
    }

    const currentUserId = this.auth.getUserId();
    if (!currentUserId) {
      alert('Tu dois être connecté pour réserver.');
      return;
    }

    const payload: CreateBookingDto = {
      statutBooking: 'ACCEPTEE',
      dateBooking: this.toLocalIso(chosenDay.date, chosenSlot.label),
      user: { idUser: currentUserId },
      babyfoot: { idBabyfoot: chosenBaby.idBabyfoot }
    };

    this.bookingService.create(payload).subscribe({
      next: (created) => {
        alert(`✅ Réservation créée (#${created.idBooking}) le ${this.formatDate(chosenDay.date)} à ${chosenSlot.label}`);
      },
      error: (err) => {
        console.error('Booking create error =>', err);
        const backendMsg = err?.error?.error;
        const httpMsg    = err?.message || 'HTTP error';
        const finalMsg   = backendMsg || httpMsg || 'Une erreur est survenue';
        alert(`❌ ${finalMsg}`);
      }
    });
  }

  /** "YYYY-MM-DDTHH:mm:00" (sans timezone) — compatible Spring LocalDateTime */
  private toLocalIso(date: Date, timeLabel: string): string {
    const [h, m] = timeLabel.split(':').map(Number);
    const d = new Date(date);
    d.setHours(h, m, 0, 0);
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:00`;
  }

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
