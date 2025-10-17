import { Routes } from '@angular/router';
import {Accueil} from './accueil/accueil';
import {Reservation} from './reservation/reservation';
import {Dashboard} from './dashboard/dashboard';
import {Register} from './register/register';
import {Login} from './login/login';
import {UserProfile} from './user-profile/user-profile';
import {AdminGuard} from './security/admin.guard';

export const routes: Routes = [
  {path: 'reservation', component: Reservation},
  {path: 'dashboard', component: Dashboard, canActivate: [AdminGuard]},
  { path: '', component: Accueil},
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'user-profile', component: UserProfile}
];
