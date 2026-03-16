import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Campaign } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {
  constructor(private http: HttpClient) {}

  findByTenantId(tenantId: number): Observable<Campaign[]> {
    return this.http.get<Campaign[]>(`/api/tenants/${tenantId}/campaigns`);
  }

  create(tenantId: number, campaign: Partial<Campaign>): Observable<Campaign> {
    return this.http.post<Campaign>(`/api/tenants/${tenantId}/campaigns`, campaign);
  }

  update(tenantId: number, id: number, campaign: Partial<Campaign>): Observable<Campaign> {
    return this.http.put<Campaign>(`/api/tenants/${tenantId}/campaigns/${id}`, campaign);
  }

  delete(tenantId: number, id: number): Observable<void> {
    return this.http.delete<void>(`/api/tenants/${tenantId}/campaigns/${id}`);
  }
}
