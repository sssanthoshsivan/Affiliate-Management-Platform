import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';
import { AnalyticsService } from '../../../core/services/analytics';
import { AffiliateService } from '../../../core/services/affiliate';
import { ItemService } from '../../../core/services/item';
import { CampaignService } from '../../../core/services/campaign';
import { TenantContextService } from '../../../core/services/tenant-context';
import { Analytics, Affiliate, Campaign, Item } from '../../../core/models/models';
import { StatCard } from '../stat-card/stat-card';
import { FilterBar } from '../filter-bar/filter-bar';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    StatCard,
    FilterBar
  ],
  templateUrl: './dashboard-home.html',
  styleUrl: './dashboard-home.scss'
})
export class DashboardHome implements OnInit {
  analytics: Analytics | null = null;
  affiliates: Affiliate[] = [];
  campaigns: Campaign[] = [];
  items: Item[] = [];
  
  loading = true;
  displayedColumns: string[] = ['name', 'orders', 'revenue', 'commission', 'conversion'];

  constructor(
    private analyticsService: AnalyticsService,
    private affiliateService: AffiliateService,
    private itemService: ItemService,
    private campaignService: CampaignService,
    private tenantContext: TenantContextService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const tenantId = this.tenantContext.getTenantId();
    if (!tenantId) {
      this.loading = false;
      return;
    }

    // Load filter data and initial analytics
    forkJoin({
      affiliates: this.affiliateService.findByTenantId(tenantId),
      items: this.itemService.findByTenantId(tenantId),
      campaigns: this.campaignService.findByTenantId(tenantId)
    }).subscribe({
      next: (data) => {
        this.affiliates = data.affiliates;
        this.items = data.items;
        this.campaigns = data.campaigns;
        this.loadAnalytics({});
      },
      error: () => this.snackBar.open('Error loading dashboard data', 'Close', { duration: 3000 })
    });
  }

  loadAnalytics(filters: any): void {
    const tenantId = this.tenantContext.getTenantId();
    if (tenantId) {
      this.loading = true;
      this.analyticsService.getAnalytics(tenantId, filters).subscribe({
        next: (data) => {
          this.analytics = data;
          this.loading = false;
        },
        error: () => {
          this.snackBar.open('Error fetching analytics', 'Close', { duration: 3000 });
          this.loading = false;
        }
      });
    }
  }

  onFilterChange(filters: any): void {
    this.loadAnalytics(filters);
  }

  getConversionRate(orders: number, clicks: number): string {
    if (!clicks) return '0%';
    return `${((orders / clicks) * 100).toFixed(1)}%`;
  }
}
