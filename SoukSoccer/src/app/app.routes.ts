import { Routes } from '@angular/router';
import {Reservation} from './reservation/reservation';
import {Dashboard} from './dashboard/dashboard';

export const routes: Routes = [
  {path: 'reservation', component: Reservation},
  {path: 'dashboard', component: Dashboard}
];
