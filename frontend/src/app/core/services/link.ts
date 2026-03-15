import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AffiliateLink, AffiliateLinkRequest, AffiliateLinkResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class LinkService {
  constructor(private http: HttpClient) {}

  generate(request: AffiliateLinkRequest): Observable<AffiliateLinkResponse> {
    return this.http.post<AffiliateLinkResponse>('/api/affiliate-links', request);
  }
}
