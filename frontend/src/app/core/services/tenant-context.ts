import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TenantContextService {
  private tenantIdSubject = new BehaviorSubject<number | null>(null);
  tenantId$ = this.tenantIdSubject.asObservable();

  setTenantId(id: number | null): void {
    this.tenantIdSubject.next(id);
    if (id) {
      localStorage.setItem('tenantId', id.toString());
    } else {
      localStorage.removeItem('tenantId');
    }
  }

  getTenantId(): number | null {
    return this.tenantIdSubject.value;
  }

  constructor() {
    const savedId = localStorage.getItem('tenantId');
    if (savedId) {
      this.tenantIdSubject.next(parseInt(savedId, 10));
    }
  }
}
