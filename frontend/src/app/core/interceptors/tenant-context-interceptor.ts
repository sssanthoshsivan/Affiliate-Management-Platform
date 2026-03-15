import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TenantContextService } from '../services/tenant-context';

export const tenantContextInterceptor: HttpInterceptorFn = (req, next) => {
  const tenantContext = inject(TenantContextService);
  const tenantId = tenantContext.getTenantId();

  if (tenantId) {
    const cloned = req.clone({
      setHeaders: {
        'X-Tenant-ID': tenantId.toString()
      }
    });
    return next(cloned);
  }

  return next(req);
};
