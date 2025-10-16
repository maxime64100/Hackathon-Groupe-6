import { Routes } from '@angular/router';
import {Accueil} from './accueil/accueil';
import {Reservation} from './reservation/reservation';
import {Dashboard} from './dashboard/dashboard';
import {Register} from './register/register';
import {Login} from './login/login';

export const routes: Routes = [
  {path: 'reservation', component: Reservation},
  {path: 'dashboard', component: Dashboard},
  { path: '', component: Accueil},
  { path: 'register', component: Register },
  { path: 'login', component: Login }
];
