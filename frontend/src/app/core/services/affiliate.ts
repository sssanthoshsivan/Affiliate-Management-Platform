import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Affiliate } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AffiliateService {
  constructor(private http: HttpClient) {}

  findByTenantId(tenantId: number): Observable<Affiliate[]> {
    return this.http.get<Affiliate[]>(`/api/tenants/${tenantId}/affiliates`);
  }

  findById(tenantId: number, id: number): Observable<Affiliate> {
    return this.http.get<Affiliate>(`/api/tenants/${tenantId}/affiliates/${id}`);
  }

  create(tenantId: number, affiliate: Partial<Affiliate>): Observable<Affiliate> {
    return this.http.post<Affiliate>(`/api/tenants/${tenantId}/affiliates`, affiliate);
  }

  update(tenantId: number, id: number, affiliate: Partial<Affiliate>): Observable<Affiliate> {
    return this.http.put<Affiliate>(`/api/tenants/${tenantId}/affiliates/${id}`, affiliate);
  }

  delete(tenantId: number, id: number): Observable<void> {
    return this.http.delete<void>(`/api/tenants/${tenantId}/affiliates/${id}`);
  }
}
