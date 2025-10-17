import { Routes } from '@angular/router';
import {Accueil} from './accueil/accueil';
import {Reservation} from './reservation/reservation';
import {Dashboard} from './dashboard/dashboard';
import {Register} from './register/register';
import {Login} from './login/login';
import {UserProfile} from './user-profile/user-profile';
import {AdminGuard} from './security/admin.guard';
import {UserGuard} from './security/user.guard';

export const routes: Routes = [
  {path: 'reservation', component: Reservation, canActivate: [UserGuard]},
  {path: 'dashboard', component: Dashboard, canActivate: [AdminGuard]},
  { path: '', component: Accueil},
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'user-profile', component: UserProfile, canActivate: [UserGuard]}
];
