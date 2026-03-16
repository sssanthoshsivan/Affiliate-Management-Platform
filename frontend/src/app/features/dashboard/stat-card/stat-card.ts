import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <mat-card class="stat-card glass-card" [style.border-top]="'4px solid ' + color">
      <mat-card-content>
        <div class="stat-content">
          <div class="stat-info">
            <span class="stat-label">{{ label }}</span>
            <h2 class="stat-value">{{ value }}</h2>
          </div>
          <div class="stat-icon-wrapper" [style.background]="'linear-gradient(135deg, ' + color + '10 0%, ' + color + '25 100%)'" [style.color]="color">
            <mat-icon>{{ icon }}</mat-icon>
          </div>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styleUrl: './stat-card.scss'
})
export class StatCard {
  @Input() label: string = '';
  @Input() value: string | number = '';
  @Input() icon: string = '';
  @Input() color: string = '#3f51b5';
}
