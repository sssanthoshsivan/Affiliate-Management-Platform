import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Affiliate, Campaign, Item } from '../../../core/models/models';

@Component({
  selector: 'app-filter-bar',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './filter-bar.html',
  styleUrl: './filter-bar.scss'
})
export class FilterBar implements OnInit {
  @Input() affiliates: Affiliate[] = [];
  @Input() campaigns: Campaign[] = [];
  @Input() items: Item[] = [];
  
  @Output() filterChange = new EventEmitter<any>();

  filterForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      affiliateId: [null],
      campaignId: [null],
      itemId: [null],
      from: [null],
      to: [null]
    });
  }

  ngOnInit(): void {}

  applyFilters(): void {
    const rawValues = this.filterForm.value;
    const formatted = {
      ...rawValues,
      from: rawValues.from ? rawValues.from.toISOString() : null,
      to: rawValues.to ? rawValues.to.toISOString() : null
    };
    this.filterChange.emit(formatted);
  }

  resetFilters(): void {
    this.filterForm.reset();
    this.applyFilters();
  }
}
