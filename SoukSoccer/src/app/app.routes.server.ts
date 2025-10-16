import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '', // racine
    renderMode: RenderMode.Prerender
  },
  {
    path: 'auth',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'auth/login',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'auth/register',
    renderMode: RenderMode.Prerender
  }
];
