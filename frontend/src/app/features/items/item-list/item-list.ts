import { ChangeDetectorRef, Component, OnInit, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ItemService } from '../../../core/services/item';
import { TenantContextService } from '../../../core/services/tenant-context';
import { Item } from '../../../core/models/models';
import { CreateDialog } from '../../../shared/components/create-dialog/create-dialog';

@Component({
  selector: 'app-item-list',
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
  templateUrl: './item-list.html',
  styleUrl: './item-list.scss'
})
export class ItemList implements OnInit {
  items: Item[] = [];
  displayedColumns: string[] = ['id', 'name', 'type', 'category', 'price', 'actions'];
  loading = true;

  constructor(
    private itemService: ItemService,
    private tenantContext: TenantContextService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    this.tenantContext.tenantId$.subscribe(tenantId => {
      if (tenantId) {
        this.loadItems(tenantId);
      } else {
        this.loading = false;
      }
    });
  }

  loadItems(tenantId: number): void {
    this.loading = true;
    this.itemService.findByTenantId(tenantId).subscribe({
      next: (data) => {
        this.zone.run(() => {
          this.items = data;
          this.loading = false;
          this.cdr.detectChanges();
        });
      },
      error: () => {
        this.zone.run(() => {
          this.snackBar.open('Error loading items', 'Close', { duration: 3000 });
          this.loading = false;
          this.cdr.detectChanges();
        });
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: {
        title: 'Add New Item',
        fields: [
          { name: 'name', label: 'Item Name', type: 'text', required: true },
          { name: 'type', label: 'Type', type: 'select', required: true, 
            options: [{value: 'PRODUCT', label: 'Product'}, {value: 'SERVICE', label: 'Service'}] 
          },
          { name: 'category', label: 'Category', type: 'text', required: false },
          { name: 'price', label: 'Price', type: 'number', required: true }
        ]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const tenantId = this.tenantContext.getTenantId();
        if (tenantId) {
          this.itemService.create(tenantId, result).subscribe({
            next: () => {
              this.snackBar.open('Item created', 'Close', { duration: 3000 });
              this.loadItems(tenantId);
            },
            error: () => this.snackBar.open('Error creating item', 'Close', { duration: 3000 })
          });
        }
      }
    });
  }

  openEditDialog(item: Item): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: {
        title: 'Edit Item',
        fields: [
          { name: 'name', label: 'Item Name', type: 'text', required: true, value: item.name, initialValue: item.name },
          { name: 'type', label: 'Type', type: 'select', required: true, 
            options: [{value: 'PRODUCT', label: 'Product'}, {value: 'SERVICE', label: 'Service'}],
            value: item.type,
            initialValue: item.type
          },
          { name: 'category', label: 'Category', type: 'text', required: false, value: item.category, initialValue: item.category },
          { name: 'price', label: 'Price', type: 'number', required: true, value: item.price, initialValue: item.price }
        ]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const tenantId = this.tenantContext.getTenantId();
        if (tenantId) {
          this.itemService.update(tenantId, item.id, result).subscribe({
            next: () => {
              this.snackBar.open('Item updated', 'Close', { duration: 3000 });
              this.loadItems(tenantId);
            },
            error: () => this.snackBar.open('Error updating item', 'Close', { duration: 3000 })
          });
        }
      }
    });
  }

  deleteItem(id: number): void {
    if (confirm('Are you sure you want to delete this item?')) {
      const tenantId = this.tenantContext.getTenantId();
      if (tenantId) {
        this.itemService.delete(tenantId, id).subscribe({
          next: () => {
            this.snackBar.open('Item deleted', 'Close', { duration: 3000 });
            this.loadItems(tenantId);
          },
          error: () => this.snackBar.open('Error deleting item', 'Close', { duration: 3000 })
        });
      }
    }
  }
}
