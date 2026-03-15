import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TenantService } from '../../../core/services/tenant';
import { Tenant } from '../../../core/models/models';
import { CreateDialog } from '../../../shared/components/create-dialog/create-dialog';

@Component({
  selector: 'app-tenant-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './tenant-list.html',
  styleUrl: './tenant-list.scss'
})
export class TenantList implements OnInit {
  tenants: Tenant[] = [];
  displayedColumns: string[] = ['id', 'name', 'domain', 'commissionRate', 'status', 'actions'];
  loading = true;

  constructor(
    private tenantService: TenantService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadTenants();
  }

  loadTenants(): void {
    this.loading = true;
    this.tenantService.findAll().subscribe({
      next: (data) => {
        this.tenants = data;
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Error loading tenants', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: {
        title: 'Register New Tenant',
        fields: [
          { name: 'name', label: 'Tenant Name', type: 'text', required: true },
          { name: 'domain', label: 'Domain', type: 'text', required: true },
          { name: 'commissionRate', label: 'Default Commission (%)', type: 'number', required: true }
        ]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.tenantService.create(result).subscribe({
          next: () => {
            this.snackBar.open('Tenant registered', 'Close', { duration: 3000 });
            this.loadTenants();
          },
          error: () => this.snackBar.open('Error registering tenant', 'Close', { duration: 3000 })
        });
      }
    });
  }

  updateCommission(tenant: Tenant): void {
    const newRate = prompt('Enter new commission rate (%)', tenant.commissionRate.toString());
    if (newRate !== null) {
      const rate = parseFloat(newRate);
      if (!isNaN(rate)) {
        this.tenantService.updateCommissionRate(tenant.id, rate).subscribe({
          next: () => {
            this.snackBar.open('Commission rate updated', 'Close', { duration: 3000 });
            this.loadTenants();
          },
          error: () => this.snackBar.open('Error updating rate', 'Close', { duration: 3000 })
        });
      }
    }
  }
}
