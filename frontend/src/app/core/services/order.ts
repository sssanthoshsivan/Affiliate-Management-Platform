import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderRequest {
  tenantId: number;
  affiliateId: number;
  itemId: number;
  campaignId?: number | null;
  orderValue: number;
}

export interface OrderResponse {
  id: number;
  tenantId: number;
  affiliateId: number;
  itemId: number;
  campaignId?: number;
  orderValue: number;
  commission: number;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  constructor(private http: HttpClient) {}

  recordOrder(request: OrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>('/api/orders', request);
  }
}
