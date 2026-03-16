import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AffiliateService } from '../../../core/services/affiliate';
import { TenantContextService } from '../../../core/services/tenant-context';
import { Affiliate } from '../../../core/models/models';
import { CreateDialog } from '../../../shared/components/create-dialog/create-dialog';

@Component({
  selector: 'app-affiliate-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDialogModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './affiliate-list.html',
  styleUrl: './affiliate-list.scss'
})
export class AffiliateList implements OnInit {
  affiliates: Affiliate[] = [];
  displayedColumns: string[] = ['id', 'name', 'email', 'status', 'createdAt', 'actions'];
  loading = true;

  constructor(
    private affiliateService: AffiliateService,
    private tenantContext: TenantContextService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.tenantContext.tenantId$.subscribe(tenantId => {
      if (tenantId) {
        this.loadAffiliates(tenantId);
      } else {
        this.loading = false;
      }
    });
  }

  loadAffiliates(tenantId: number): void {
    this.loading = true;
    this.affiliateService.findByTenantId(tenantId).subscribe({
      next: (data) => {
        this.affiliates = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.snackBar.open('Failed to load affiliates', 'Close', { duration: 3000 });
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: { 
        title: 'Add New Affiliate',
        fields: [
          { name: 'name', label: 'Full Name', type: 'text', required: true },
          { name: 'email', label: 'Email Address', type: 'email', required: true }
        ]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const tenantId = this.tenantContext.getTenantId();
        if (tenantId) {
          this.affiliateService.create(tenantId, result).subscribe({
            next: () => {
              this.snackBar.open('Affiliate created successfully', 'Close', { duration: 3000 });
              this.loadAffiliates(tenantId);
            },
            error: () => this.snackBar.open('Error creating affiliate', 'Close', { duration: 3000 })
          });
        }
      }
    });
  }
}
