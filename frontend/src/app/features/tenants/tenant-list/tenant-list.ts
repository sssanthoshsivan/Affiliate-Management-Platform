import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
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
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Error loading tenants', 'Close', { duration: 3000 });
        this.loading = false;
        this.cdr.detectChanges();
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

  openEditDialog(tenant: Tenant): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: {
        title: 'Edit Tenant',
        fields: [
          { name: 'name', label: 'Tenant Name', type: 'text', required: true, value: tenant.name },
          { name: 'domain', label: 'Domain', type: 'text', required: true, value: tenant.domain },
          { name: 'commissionRate', label: 'Commission Rate (%)', type: 'number', required: true, value: tenant.commissionRate },
          { 
            name: 'status', 
            label: 'Status', 
            type: 'select', 
            options: [
              { label: 'Active', value: 'ACTIVE' },
              { label: 'In-Active', value: 'INACTIVE' }
            ],
            value: tenant.status 
          }
        ]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.tenantService.update(tenant.id, result).subscribe({
          next: () => {
            this.snackBar.open('Tenant updated', 'Close', { duration: 3000 });
            this.loadTenants();
          },
          error: () => this.snackBar.open('Error updating tenant', 'Close', { duration: 3000 })
        });
      }
    });
  }

  deleteTenant(id: number): void {
    if (confirm('Are you sure you want to delete this tenant? This will also delete all associated affiliates, links, and orders.')) {
      this.tenantService.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Tenant deleted', 'Close', { duration: 3000 });
          this.loadTenants();
        },
        error: () => this.snackBar.open('Error deleting tenant', 'Close', { duration: 3000 })
      });
    }
  }
}
