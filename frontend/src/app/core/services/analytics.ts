import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Analytics } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  constructor(private http: HttpClient) {}

  getAnalytics(tenantId: number, filters: any): Observable<Analytics> {
    let params = new HttpParams();
    
    if (filters.affiliateId) params = params.set('affiliateId', filters.affiliateId);
    if (filters.campaignId) params = params.set('campaignId', filters.campaignId);
    if (filters.itemId) params = params.set('itemId', filters.itemId);
    if (filters.from) params = params.set('from', filters.from);
    if (filters.to) params = params.set('to', filters.to);

    return this.http.get<Analytics>(`/api/tenants/${tenantId}/analytics`, { params });
  }
}
