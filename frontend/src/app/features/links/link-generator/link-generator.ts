import { ChangeDetectorRef, Component, OnInit, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { Affiliate, Item, Campaign, AffiliateLinkResponse } from '../../../core/models/models';
import { AffiliateService } from '../../../core/services/affiliate';
import { ItemService } from '../../../core/services/item';
import { CampaignService } from '../../../core/services/campaign';
import { LinkService } from '../../../core/services/link';
import { TenantContextService } from '../../../core/services/tenant-context';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-link-generator',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSnackBarModule,
    ClipboardModule
  ],
  templateUrl: './link-generator.html',
  styleUrl: './link-generator.scss'
})
export class LinkGenerator implements OnInit {
  generatorForm: FormGroup;
  affiliates: Affiliate[] = [];
  items: Item[] = [];
  campaigns: Campaign[] = [];
  generatedLink: AffiliateLinkResponse | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private affiliateService: AffiliateService,
    private itemService: ItemService,
    private campaignService: CampaignService,
    private linkService: LinkService,
    private tenantContext: TenantContextService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.generatorForm = this.fb.group({
      affiliateId: ['', Validators.required],
      itemId: ['', Validators.required],
      campaignId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const tenantId = this.tenantContext.getTenantId();
    if (tenantId) {
      forkJoin({
        affiliates: this.affiliateService.findByTenantId(tenantId),
        items: this.itemService.findByTenantId(tenantId),
        campaigns: this.campaignService.findByTenantId(tenantId)
      }).subscribe(data => {
        this.affiliates = data.affiliates;
        this.items = data.items;
        this.campaigns = data.campaigns;
        this.cdr.detectChanges();
      });
    }
  }

  generateLink(): void {
    if (this.generatorForm.valid) {
      this.loading = true;
      const request: any = {
        ...this.generatorForm.value,
        tenantId: this.tenantContext.getTenantId()
      };

      this.linkService.generate(request).subscribe({
        next: (response) => {
          this.generatedLink = response;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.snackBar.open('Error generating link', 'Close', { duration: 3000 });
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  copyToClipboard(text: string): void {
    this.snackBar.open('Copied to clipboard!', 'Close', { duration: 2000 });
  }
}
