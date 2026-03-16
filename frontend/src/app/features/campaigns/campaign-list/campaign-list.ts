import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CampaignService } from '../../../core/services/campaign';
import { TenantContextService } from '../../../core/services/tenant-context';
import { Campaign } from '../../../core/models/models';
import { CreateDialog } from '../../../shared/components/create-dialog/create-dialog';

@Component({
  selector: 'app-campaign-list',
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
  templateUrl: './campaign-list.html',
  styleUrl: './campaign-list.scss'
})
export class CampaignList implements OnInit {
  campaigns: Campaign[] = [];
  displayedColumns: string[] = ['id', 'name', 'description', 'createdAt', 'actions'];
  loading = true;

  constructor(
    private campaignService: CampaignService,
    private tenantContext: TenantContextService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.tenantContext.tenantId$.subscribe(tenantId => {
      if (tenantId) {
        this.loadCampaigns(tenantId);
      } else {
        this.loading = false;
      }
    });
  }

  loadCampaigns(tenantId: number): void {
    this.loading = true;
    this.campaignService.findByTenantId(tenantId).subscribe({
      next: (data) => {
        this.campaigns = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.snackBar.open('Error loading campaigns', 'Close', { duration: 3000 });
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: {
        title: 'Create New Campaign',
        fields: [
          { name: 'name', label: 'Campaign Name', type: 'text', required: true },
          { name: 'description', label: 'Description', type: 'textarea', required: false }
        ]
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const tenantId = this.tenantContext.getTenantId();
        if (tenantId) {
          this.campaignService.create(tenantId, result).subscribe({
            next: () => {
              this.snackBar.open('Campaign created', 'Close', { duration: 3000 });
              this.loadCampaigns(tenantId);
            },
            error: () => this.snackBar.open('Error creating campaign', 'Close', { duration: 3000 })
          });
        }
      }
    });
  }
}
