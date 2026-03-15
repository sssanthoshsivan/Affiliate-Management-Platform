import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TenantContextService } from './core/services/tenant-context';
import { TenantService } from './core/services/tenant';
import { Tenant } from './core/models/models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatSelectModule,
    MatFormFieldModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  protected readonly title = signal('RunLoyal Affiliate Platform');
  tenants$: Observable<Tenant[]> | undefined;
  selectedTenantId: number | null = null;

  constructor(
    private tenantContext: TenantContextService,
    private tenantService: TenantService
  ) {}

  ngOnInit(): void {
    this.tenants$ = this.tenantService.findAll();
    this.selectedTenantId = this.tenantContext.getTenantId();
  }

  onTenantChange(id: number): void {
    this.tenantContext.setTenantId(id);
    this.selectedTenantId = id;
    // Reload current route to refresh data for new tenant
    window.location.reload();
  }
}
