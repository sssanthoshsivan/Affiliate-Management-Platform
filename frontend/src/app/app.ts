import { Component, OnInit, signal, ChangeDetectorRef, ViewChild } from '@angular/core';
import { Router, RouterOutlet, RouterLink, RouterLinkActive, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
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
  isProductPage = false;

  @ViewChild('sidenav') sidenav!: MatSidenav;

  constructor(
    private tenantContext: TenantContextService,
    private tenantService: TenantService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.isProductPage = this.router.url.split('?')[0].includes('/product/');
      this.cdr.detectChanges();
    });
  }

  ngOnInit(): void {
    this.tenants$ = this.tenantService.findAll();
    this.selectedTenantId = this.tenantContext.getTenantId();

    // If no tenant selected, pick the first one from the list
    if (!this.selectedTenantId) {
      this.tenants$.subscribe(tenants => {
        if (tenants?.length > 0) {
          const firstTenantId = tenants[0].id!;
          this.tenantContext.setTenantId(firstTenantId);
          this.selectedTenantId = firstTenantId;
        }
      });
    }
  }

  onTenantChange(id: number): void {
    this.tenantContext.setTenantId(id);
    this.selectedTenantId = id;
  }

  toggleSidenav(): void {
    if (this.sidenav) {
      this.sidenav.toggle();
    }
  }
}
