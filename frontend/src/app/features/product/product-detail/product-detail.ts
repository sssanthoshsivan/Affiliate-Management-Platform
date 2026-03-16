import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ItemService } from '../../../core/services/item';
import { OrderService } from '../../../core/services/order';
import { Item } from '../../../core/models/models';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CommonModule,
    CurrencyPipe,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.scss'
})
export class ProductDetail implements OnInit {
  item: Item | null = null;
  loading = true;
  ordering = false;
  orderPlaced = false;
  orderCommission: number | null = null;
  error: string | null = null;

  // Tracking params from URL
  tenantId: number | null = null;
  affiliateId: number | null = null;
  campaignId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private itemService: ItemService,
    private orderService: OrderService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    const itemId = Number(this.route.snapshot.paramMap.get('id'));
    const query = this.route.snapshot.queryParamMap;

    this.tenantId = query.get('tenantId') ? Number(query.get('tenantId')) : null;
    this.affiliateId = query.get('affiliateId') ? Number(query.get('affiliateId')) : null;
    this.campaignId = query.get('campaignId') ? Number(query.get('campaignId')) : null;

    if (!this.tenantId || !itemId) {
      this.error = 'Invalid product link. Please contact support.';
      this.loading = false;
      return;
    }

    this.itemService.findById(this.tenantId, itemId).subscribe({
      next: (item) => {
        this.zone.run(() => {
          this.item = item;
          this.loading = false;
          this.cdr.detectChanges();
        });
      },
      error: () => {
        this.zone.run(() => {
          this.error = 'Product not found or no longer available.';
          this.loading = false;
          this.cdr.detectChanges();
        });
      }
    });
  }

  placeOrder(): void {
    if (!this.item || !this.tenantId || !this.affiliateId) return;

    this.ordering = true;
    this.orderService.recordOrder({
      tenantId: this.tenantId,
      affiliateId: this.affiliateId,
      itemId: this.item.id,
      campaignId: this.campaignId,
      orderValue: Number(this.item.price)
    }).subscribe({
      next: (res) => {
        this.zone.run(() => {
          this.orderPlaced = true;
          this.orderCommission = res.commission;
          this.ordering = false;
          this.cdr.detectChanges();
          this.snackBar.open('🎉 Order placed successfully!', 'Close', { duration: 4000 });
        });
      },
      error: () => {
        this.zone.run(() => {
          this.ordering = false;
          this.cdr.detectChanges();
          this.snackBar.open('Failed to place order. Please try again.', 'Close', { duration: 4000 });
        });
      }
    });
  }
}
