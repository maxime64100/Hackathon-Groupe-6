import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';

type StatusColor = 'success' | 'warning' | 'danger' | 'secondary';

interface StatCard {
  icon: string;           // bootstrap icon class
  value: string | number;
  label: string;
  delta?: string;         // +5%, etc.
  pillColor?: 'primary' | 'success' | 'warning' | 'danger' | 'secondary';
}

interface TableItem {
  id: number;
  name: string;           // "Babyfoot #1"
  zone: string;           // "Principale"
  status: 'Actif' | 'Maintenance';
  utilization?: number;   // 0..100
  gamesToday?: number;
  issue?: string;         // "Barres bloquées"
  sinceDays?: number;     // 2
}

interface UserItem {
  id: number;
  name: string;
  email: string;
  role: 'Admin' | 'Modérateur' | 'Étudiant';
  online?: boolean;
  lastActive?: string;    // "il y a 2 h"
  avatarUrl: string;
}

interface ActivityItem {
  icon: string;
  text: string;
  time: string;           // "il y a 5 min"
  hint?: string;
  badgeColor?: 'primary'|'success'|'warning'|'danger'|'secondary';
}

interface SystemProbe {
  name: string;           // "API"
  status: 'Opérationnel' | 'Partiel' | 'Incident';
  color: 'success' | 'warning' | 'danger';
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard {

  // ====== CARDS KPIs (haut) ======
  statCards = signal<StatCard[]>([
    // TODO Données à changer (GET /admin/stats)
    { icon: 'bi-controller', value: 12,  label: 'Babyfoots actifs',   delta: '+5%', pillColor: 'success' },
    { icon: 'bi-people',     value: 847, label: 'Utilisateurs actifs', delta: '+12%', pillColor: 'primary' },
    { icon: 'bi-calendar-check', value: 156, label: 'Réservations aujourd\'hui', delta: '+8%', pillColor: 'warning' },
    { icon: 'bi-tools',      value: 2,   label: 'En maintenance',     delta: '',    pillColor: 'danger' },
  ]);

  // ====== GESTION BABYFOOTS ======
  tables = signal<TableItem[]>([
    // TODO Données à changer (GET /tables)
    { id: 1, name: 'Babyfoot #1', zone: 'Principale', status: 'Actif', utilization: 87, gamesToday: 23 },
    { id: 3, name: 'Babyfoot #3', zone: 'Détente',    status: 'Actif', utilization: 65, gamesToday: 18 },
    { id: 7, name: 'Babyfoot #7', zone: 'Calme',      status: 'Maintenance', issue: 'Barres bloquées', sinceDays: 2 },
  ]);

  // ====== GESTION UTILISATEURS ======
  users = signal<UserItem[]>([
    // TODO Données à changer (GET /users?limit=3&sort=lastActive)
    { id: 1, name: 'Alexandre Martin', email: 'alexandre.martin@ynov.com', role: 'Admin', lastActive: 'il y a 2 h', avatarUrl: 'https://i.pravatar.cc/48?img=15' },
    { id: 2, name: 'Sophie Dubois',    email: 'sophie.dubois@ynov.com',    role: 'Étudiant', online: true, avatarUrl: 'https://i.pravatar.cc/48?img=47' },
    { id: 3, name: 'Thomas Leroy',     email: 'thomas.leroy@ynov.com',     role: 'Modérateur', lastActive: 'il y a 1 h', avatarUrl: 'https://i.pravatar.cc/48?img=22' },
  ]);

  // ====== ACTIONS RAPIDES ======
  quickActions = signal([
    // TODO : brancher vers routes / modales réelles
    { icon: 'bi-plus-lg',        label: 'Ajouter un babyfoot',    action: () => this.onAddTable() },
    { icon: 'bi-person-plus',    label: 'Inviter un utilisateur', action: () => this.onInviteUser() },
    { icon: 'bi-wrench',         label: 'Signaler maintenance',   action: () => this.onReportMaintenance() },
    { icon: 'bi-graph-up',       label: 'Voir les rapports',      action: () => this.onOpenReports() },
  ]);

  // ====== ACTIVITÉ RÉCENTE ======
  activities = signal<ActivityItem[]>([
    // TODO Données à changer (GET /activities?limit=5)
    { icon: 'bi-person-check', text: 'Nouvel utilisateur inscrit', time: 'il y a 5 min', hint: 'Marie Durand', badgeColor: 'success' },
    { icon: 'bi-tools',        text: 'Maintenance terminée',       time: 'il y a 1 h',   hint: 'Babyfoot #5', badgeColor: 'primary' },
    { icon: 'bi-lightning',    text: 'Pic de réservations',        time: 'il y a 2 h',   hint: '47 réservations', badgeColor: 'warning' },
  ]);

  // ====== ÉTAT DU SYSTÈME ======
  systemStatus = signal<SystemProbe[]>([
    // TODO Données à changer (GET /health)
    { name: 'API',         status: 'Opérationnel', color: 'success' },
    { name: 'Base de données', status: 'Opérationnel', color: 'success' },
    { name: 'Capteurs IoT',    status: 'Partiel',      color: 'warning' },
    { name: 'Notifications',   status: 'Opérationnel', color: 'success' },
  ]);

  // ====== Handlers (placeholder) ======
  onAddTable() { /* TODO: open modal / route */ alert('TODO Ajouter un babyfoot'); }
  onInviteUser() { /* TODO */ alert('TODO Inviter un utilisateur'); }
  onReportMaintenance() { /* TODO */ alert('TODO Signaler maintenance'); }
  onOpenReports() { /* TODO */ alert('TODO Voir les rapports'); }

  editTable(t: TableItem) { /* TODO */ alert('TODO Edit ' + t.name); }
  removeTable(t: TableItem) { /* TODO */ if (confirm('Supprimer ' + t.name + ' ?')) alert('TODO DELETE'); }
  viewTable(t: TableItem) { /* TODO */ alert('TODO Détails ' + t.name); }

  inviteUser() { /* TODO */ alert('TODO Inviter'); }
  openUsers() { /* TODO */ alert('TODO Voir tous les utilisateurs'); }

  // Helpers
  statusBadgeClass(s: TableItem['status']) {
    return s === 'Actif' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger';
  }
}
