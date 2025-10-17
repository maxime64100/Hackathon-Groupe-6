import {Component, inject, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';
import {HttpClientModule} from '@angular/common/http';
import {UserBabyfoot, UserService} from '../service/user.service';
import {Babyfoot, BabyfootService} from '../service/babyfoot.service';

type StatusColor = 'success' | 'warning' | 'danger' | 'secondary';

interface StatCard {
  icon: string;
  value: string | number;
  label: string;
  delta?: string;
  pillColor?: 'primary' | 'success' | 'warning' | 'danger' | 'secondary';
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
  private babyfootService = inject(BabyfootService);

  // ====== CARDS KPIs (haut) ======
  statCards = signal<StatCard[]>([]);

  // ====== GESTION BABYFOOTS ======
  tables = signal<Babyfoot[]>([]);

  // ====== GESTION UTILISATEURS (dynamique depuis API) ======
  users = signal<UserItem[]>([]);
  page = signal(0);
  pageSize = signal(5);
  totalPages = signal(0);
  isLoading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loadUsers();
    this.loadStats();
    this.loadTables();
  }

  loadTables() {
    this.babyfootService.getAll().subscribe({
      next: (data: Babyfoot[]) => {
        this.tables.set(data);
      },
      error: (err: any) => {
        console.error('Erreur chargement babyfoots', err);
        alert('Erreur lors du chargement des babyfoots ❌');
      }
    });
  }

  onAddTable() {
    const place = prompt("📍 Emplacement du babyfoot ?");
    if (!place) return;

    const statut = prompt("⚙️ Statut du babyfoot ? (free / occupied / maintenance)", "free") as
      | 'free'
      | 'occupied'
      | 'maintenance'
      | null;

    if (!statut || !['free', 'occupied', 'maintenance'].includes(statut)) {
      alert("❌ Statut invalide");
      return;
    }

    const newBabyfoot: Partial<Babyfoot> = {
      place,
      statutBabyfoot: statut
    };

    this.babyfootService.create(newBabyfoot).subscribe({
      next: (created) => {
        alert(`✅ Babyfoot ajouté avec succès (${created.place})`);
        this.tables.update(list => [...list, created]);
      },
      error: (err) => {
        console.error('Erreur création babyfoot:', err);
        alert("❌ Erreur lors de la création du babyfoot");
      }
    });
  }

  editTable(t: Babyfoot) {
    const newPlace = prompt("📍 Nouvel emplacement du babyfoot :", t.place);
    if (newPlace === null) return; // Annulé

    const newStatut = prompt("⚙️ Nouveau statut ? (free / occupied / maintenance)", t.statutBabyfoot) as
      | 'free'
      | 'occupied'
      | 'maintenance'
      | null;

    if (!newStatut || !['free', 'occupied', 'maintenance'].includes(newStatut)) {
      alert("❌ Statut invalide");
      return;
    }

    const updatedBabyfoot: Partial<Babyfoot> = {
      place: newPlace,
      statutBabyfoot: newStatut,
    };

    this.babyfootService.update(t.idBabyfoot!, updatedBabyfoot).subscribe({
      next: (updated) => {
        alert(`✅ Babyfoot #${updated.idBabyfoot} mis à jour !`);
        this.tables.update(list =>
          list.map(item => (item.idBabyfoot === t.idBabyfoot ? updated : item))
        );
        this.loadStats();
      },
      error: (err) => {
        console.error('Erreur mise à jour babyfoot:', err);
        alert("❌ Erreur lors de la mise à jour du babyfoot");
      }
    });
  }

  removeTable(t: Babyfoot) {
    if (!confirm('Supprimer ' + t.idBabyfoot + ' ?')) return;
    this.babyfootService.delete(t.idBabyfoot).subscribe({
      next: () => {
        this.tables.update(list => list.filter(item => item.idBabyfoot !== t.idBabyfoot));
        alert('✅ Babyfoot supprimé');
      },
      error: (err) => {
        console.error(err);
        alert('Erreur suppression babyfoot ❌');
      }
    });
  }

  loadStats() {
    this.userService.getStats().subscribe({
      next: (data) => {
        this.statCards.set([
          { icon: 'bi-controller', value: data.babyfootsActifs, label: 'Babyfoots actifs', pillColor: 'success' },
          { icon: 'bi-people', value: data.usersActifs, label: 'Utilisateurs actifs', pillColor: 'primary' },
          { icon: 'bi-calendar-check', value: data.reservationsAujourdhui, label: 'Réservations aujourd\'hui', pillColor: 'warning' },
          { icon: 'bi-tools', value: data.enMaintenance, label: 'En maintenance', pillColor: 'danger' },
        ]);
      },
      error: (err) => {
        console.error('Erreur chargement stats', err);
        this.statCards.set([
          { icon: 'bi-controller', value: '-', label: 'Babyfoots actifs', pillColor: 'secondary' },
          { icon: 'bi-people', value: '-', label: 'Utilisateurs actifs', pillColor: 'secondary' },
          { icon: 'bi-calendar-check', value: '-', label: 'Réservations aujourd\'hui', pillColor: 'secondary' },
          { icon: 'bi-tools', value: '-', label: 'En maintenance', pillColor: 'secondary' },
        ]);
      }
    });
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
  onReportMaintenance() { alert('TODO Signaler maintenance'); }
  onOpenReports() { alert('TODO Voir les rapports'); }
  viewTable(t: Babyfoot) { alert('TODO Détails ' + t.idBabyfoot); }

  inviteUser() {
    const name = prompt("Prénom de l'utilisateur ?");
    if (!name) return;

    const surname = prompt("Nom de l'utilisateur ?");
    if (!surname) return;

    const mail = prompt("Adresse e-mail ?");
    if (!mail) return;

    const role = prompt("Rôle (ADMIN ou USER) ?", "USER");
    if (!role) return;

    const passwordUser = prompt("Mot de passe temporaire ?");
    if (!passwordUser) return;

    const newUser: Partial<UserBabyfoot> = {
      name,
      surname,
      mail,
      passwordUser,
      role,
    };

    this.userService.createUser(newUser).subscribe({
      next: (message: any) => {
        alert(`✅ Utilisateur créé avec succès : ${message}`);
        location.reload();
      },
      error: (err: any) => {
        console.error('Erreur API:', err);
        const msg =
          err?.error && typeof err.error === 'string'
            ? err.error
            : "Erreur lors de la création de l'utilisateur ❌";
        alert(msg);
      },
    });
  }


  // ===== Helpers =====
  statusBadgeClass(s: Babyfoot['statutBabyfoot']) {
    switch (s) {
      case 'free':
        return 'bg-success-subtle text-success';
      case 'occupied':
        return 'bg-warning-subtle text-warning';
      case 'maintenance':
        return 'bg-danger-subtle text-danger';
      default:
        return 'bg-secondary-subtle text-secondary';
    }
  }
}
