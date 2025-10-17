import {Component, inject, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';
import {UserBabyfoot, UserService} from '../service/user.service';
import {AuthService} from '../service/auth.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.css']
})
export class UserProfile implements OnInit {
  userService = inject(UserService);
  authService = inject(AuthService);

  // ✅ On initialise directement avec des valeurs par défaut
  currentUser: UserBabyfoot = {
    idUser: 0,
    mail: '',
    name: '',
    surname: '',
    profileImageUrl: 'assets/images/default-avatar.jpg'
  };

  userDraft: UserBabyfoot = { ...this.currentUser };
  avatarUrl = signal<string>(this.currentUser.profileImageUrl!);
  isSaving = false;
  saveMsg: string | null = null;

  ngOnInit() {
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      const id = Number(localStorage.getItem('userId'));
      if (id) {
        this.userService.getUserById(id).subscribe({
          next: (user) => {
            this.currentUser = user;
            this.userDraft = { ...user };
            this.avatarUrl.set(user.profileImageUrl || 'assets/images/default-avatar.jpg');
          },
          error: () => console.error('❌ Impossible de charger l’utilisateur')
        });
      }
    }
  }

  onAvatarUrlChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.avatarUrl.set(input.value || 'assets/images/default-avatar.jpg');
  }

  resetAvatar() {
    this.avatarUrl.set(this.currentUser.profileImageUrl || 'assets/images/default-avatar.jpg');
  }

  onSubmit() {
    console.log('🟢 Bouton Enregistrer cliqué');
    if (!this.currentUser?.idUser) {
      console.warn('⚠️ Aucun utilisateur connecté');
      return;
    }
    this.isSaving = true;
    this.saveMsg = null;

    const update: Partial<UserBabyfoot> = {
      name: this.userDraft.name,
      surname: this.userDraft.surname,
      profileImageUrl: this.avatarUrl()
    };

    this.userService.updateUser(this.currentUser.idUser, update).subscribe({
      next: (updated) => {
        console.log('✅ Requête envoyée, réponse :', updated);
        this.currentUser = updated;
        this.userDraft = { ...updated };
        this.isSaving = false;
        this.saveMsg = '✅ Profil enregistré avec succès';
      },
      error: (err) => {
        console.error('❌ Erreur requête updateUser', err);
        this.isSaving = false;
        this.saveMsg = '❌ Erreur lors de la sauvegarde';
      }
    });
    location.reload();
  }

  onCancel() {
    this.userDraft = { ...this.currentUser };
    this.avatarUrl.set(this.currentUser.profileImageUrl || 'assets/images/default-avatar.jpg');
    this.saveMsg = null;
  }
}
