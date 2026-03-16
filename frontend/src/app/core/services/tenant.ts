import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tenant } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class TenantService {
  private apiUrl = '/api/tenants';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Tenant[]> {
    return this.http.get<Tenant[]>(this.apiUrl);
  }

  findById(id: number): Observable<Tenant> {
    return this.http.get<Tenant>(`${this.apiUrl}/${id}`);
  }

  create(tenant: Partial<Tenant>): Observable<Tenant> {
    return this.http.post<Tenant>(this.apiUrl, tenant);
  }

  updateCommissionRate(id: number, rate: number): Observable<Tenant> {
    return this.http.patch<Tenant>(`${this.apiUrl}/${id}`, { commissionRate: rate });
  }

  update(id: number, tenant: Partial<Tenant>): Observable<Tenant> {
    return this.http.put<Tenant>(`${this.apiUrl}/${id}`, tenant);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
