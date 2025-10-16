import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';

type Lang = 'fr' | 'en';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslatePipe],
  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.css']
})
export class UserProfile {
  // ===== Mock utilisateur courant (TODO Données à changer via GET /me) =====
  currentUser = {
    id: 42,
    email: 'alexandre.martin@ynov.com', // non modifiable ici
    firstName: 'Alexandre',
    lastName: 'Martin',
    avatarUrl: 'https://i.pravatar.cc/120?img=15'
  };

  // Brouillon pour le front uniquement
  userDraft = { ...this.currentUser };
  avatarUrl = signal<string>(this.currentUser.avatarUrl);

  isSaving = false;
  saveMsg: string | null = null;

  onAvatarChange(ev: Event) {
    // TODO: uploader au back plus tard
    const input = ev.target as HTMLInputElement;
    if (!input.files?.length) return;
    const file = input.files[0];
    const reader = new FileReader();
    reader.onload = () => this.avatarUrl.set(reader.result as string);
    reader.readAsDataURL(file);
  }

  resetAvatar() {
    this.avatarUrl.set(this.currentUser.avatarUrl);
  }

  onSubmit() {
    // TODO: PATCH /me plus tard
    this.isSaving = true;
    this.saveMsg = null;
    setTimeout(() => {
      this.isSaving = false;
      this.saveMsg = 'Profil enregistré ✔️ (mock)';
    }, 600);
  }

  onCancel() {
    this.userDraft = { ...this.currentUser };
    this.avatarUrl.set(this.currentUser.avatarUrl);
    this.saveMsg = null;
  }
}
