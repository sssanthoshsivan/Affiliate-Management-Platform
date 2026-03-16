import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Item } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  constructor(private http: HttpClient) {}

  findByTenantId(tenantId: number): Observable<Item[]> {
    return this.http.get<Item[]>(`/api/tenants/${tenantId}/items`);
  }

  findById(tenantId: number, id: number): Observable<Item> {
    return this.http.get<Item>(`/api/tenants/${tenantId}/items/${id}`);
  }

  create(tenantId: number, item: Partial<Item>): Observable<Item> {
    return this.http.post<Item>(`/api/tenants/${tenantId}/items`, item);
  }

  update(tenantId: number, id: number, item: Partial<Item>): Observable<Item> {
    return this.http.put<Item>(`/api/tenants/${tenantId}/items/${id}`, item);
  }

  delete(tenantId: number, id: number): Observable<void> {
    return this.http.delete<void>(`/api/tenants/${tenantId}/items/${id}`);
  }
}
