import {Component, inject, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';
import {HttpClientModule} from '@angular/common/http';
import {UserBabyfoot, UserService} from '../service/user.service';

type StatusColor = 'success' | 'warning' | 'danger' | 'secondary';

interface StatCard {
  icon: string;
  value: string | number;
  label: string;
  delta?: string;
  pillColor?: 'primary' | 'success' | 'warning' | 'danger' | 'secondary';
}

interface TableItem {
  id: number;
  name: string;
  zone: string;
  status: 'Actif' | 'Maintenance';
  utilization?: number;
  gamesToday?: number;
  issue?: string;
  sinceDays?: number;
}

interface UserItem {
  id: number;
  name: string;
  email: string;
  avatarUrl: string;
}

interface ActivityItem {
  icon: string;
  text: string;
  time: string;
  hint?: string;
  badgeColor?: 'primary' | 'success' | 'warning' | 'danger' | 'secondary';
}

interface SystemProbe {
  name: string;
  status: 'Opérationnel' | 'Partiel' | 'Incident';
  color: 'success' | 'warning' | 'danger';
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TranslatePipe, HttpClientModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {

  // ✅ Injection du service
  private userService = inject(UserService);

  // ====== CARDS KPIs (haut) ======
  statCards = signal<StatCard[]>([
    { icon: 'bi-controller', value: 12, label: 'Babyfoots actifs', delta: '+5%', pillColor: 'success' },
    { icon: 'bi-people', value: 847, label: 'Utilisateurs actifs', delta: '+12%', pillColor: 'primary' },
    { icon: 'bi-calendar-check', value: 156, label: 'Réservations aujourd\'hui', delta: '+8%', pillColor: 'warning' },
    { icon: 'bi-tools', value: 2, label: 'En maintenance', delta: '', pillColor: 'danger' },
  ]);

  // ====== GESTION BABYFOOTS ======
  tables = signal<TableItem[]>([
    { id: 1, name: 'Babyfoot #1', zone: 'Principale', status: 'Actif', utilization: 87, gamesToday: 23 },
    { id: 3, name: 'Babyfoot #3', zone: 'Détente', status: 'Actif', utilization: 65, gamesToday: 18 },
    { id: 7, name: 'Babyfoot #7', zone: 'Calme', status: 'Maintenance', issue: 'Barres bloquées', sinceDays: 2 },
  ]);

  // ====== GESTION UTILISATEURS (dynamique depuis API) ======
  users = signal<UserItem[]>([]);
  page = signal(0);
  pageSize = signal(5);
  totalPages = signal(0);
  isLoading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loadUsers();
  }

  deleteUser(u: UserItem) {
    if (!confirm(`Supprimer l'utilisateur ${u.name} ?`)) return;

    this.isLoading.set(true);

    this.userService.deleteUser(u.id).subscribe({
      next: () => {
        // Supprime l’utilisateur du signal sans recharger tout
        this.users.update(list => list.filter(item => item.id !== u.id));
        this.isLoading.set(false);
        alert(`Utilisateur ${u.name} supprimé avec succès ✅`);
      },
      error: (err) => {
        console.error(err);
        this.isLoading.set(false);
        alert(`Erreur lors de la suppression de ${u.name}`);
      }
    });
  }

  loadUsers() {
    this.isLoading.set(true);
    this.error.set(null);

    this.userService.getUsers(this.page(), this.pageSize()).subscribe({
      next: (data) => {
        this.users.set(
          data.users.map((u: UserBabyfoot) => ({
            id: u.idUser,
            name: `${u.name} ${u.surname || ''}`.trim(),
            email: u.mail,
            avatarUrl: u.profileImageUrl || '../../assets/images/default-avatar.jpg',
          }))
        );
        this.totalPages.set(data.totalPages);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.error.set('Erreur lors du chargement des utilisateurs');
        this.isLoading.set(false);
      }
    });
  }

  nextPage() {
    if (this.page() + 1 < this.totalPages()) {
      this.page.update(p => p + 1);
      this.loadUsers();
    }
  }

  prevPage() {
    if (this.page() > 0) {
      this.page.update(p => p - 1);
      this.loadUsers();
    }
  }

  // ====== ACTIONS RAPIDES ======
  quickActions = signal([
    { icon: 'bi-plus-lg', label: 'Ajouter un babyfoot', action: () => this.onAddTable() },
    { icon: 'bi-person-plus', label: 'Inviter un utilisateur', action: () => this.onInviteUser() },
    { icon: 'bi-wrench', label: 'Signaler maintenance', action: () => this.onReportMaintenance() },
    { icon: 'bi-graph-up', label: 'Voir les rapports', action: () => this.onOpenReports() },
  ]);

  // ====== ACTIVITÉ RÉCENTE ======
  activities = signal<ActivityItem[]>([
    { icon: 'bi-person-check', text: 'Nouvel utilisateur inscrit', time: 'il y a 5 min', hint: 'Marie Durand', badgeColor: 'success' },
    { icon: 'bi-tools', text: 'Maintenance terminée', time: 'il y a 1 h', hint: 'Babyfoot #5', badgeColor: 'primary' },
    { icon: 'bi-lightning', text: 'Pic de réservations', time: 'il y a 2 h', hint: '47 réservations', badgeColor: 'warning' },
  ]);

  // ====== ÉTAT DU SYSTÈME ======
  systemStatus = signal<SystemProbe[]>([
    { name: 'API', status: 'Opérationnel', color: 'success' },
    { name: 'Base de données', status: 'Opérationnel', color: 'success' },
    { name: 'Capteurs IoT', status: 'Partiel', color: 'warning' },
    { name: 'Notifications', status: 'Opérationnel', color: 'success' },
  ]);

  // ====== Handlers (placeholder) ======
  onAddTable() { alert('TODO Ajouter un babyfoot'); }
  onInviteUser() { alert('TODO Inviter un utilisateur'); }
  onReportMaintenance() { alert('TODO Signaler maintenance'); }
  onOpenReports() { alert('TODO Voir les rapports'); }
  editTable(t: TableItem) { alert('TODO Edit ' + t.name); }
  removeTable(t: TableItem) {
    if (confirm('Supprimer ' + t.name + ' ?')) alert('TODO DELETE');
  }
  viewTable(t: TableItem) { alert('TODO Détails ' + t.name); }

  inviteUser() { alert('TODO Inviter'); }
  openUsers() { alert('TODO Voir tous les utilisateurs'); }

  // ===== Helpers =====
  statusBadgeClass(s: TableItem['status']) {
    return s === 'Actif' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger';
  }
}
