// src/app/service/auth.interceptor.fn.ts
import type { HttpInterceptorFn } from '@angular/common/http';
import { HttpContextToken } from '@angular/common/http';

// Option: ignorer l'ajout du header pour certains endpoints
const SKIP_AUTH = new HttpContextToken<boolean>(() => false);
export { SKIP_AUTH };

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  try {
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;

    // Optional: don't attach token for endpoints like login/register
    const url = req.url.toLowerCase();
    if (url.endsWith('/auth/login') || url.endsWith('/auth/register')) {
      return next(req);
    }

    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next(cloned);
    }
  } catch (e) {
    // don't break requests if localStorage access échoue
    console.warn('Auth interceptor warning', e);
  }

  return next(req);
};
