import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard-module').then(m => m.DashboardModule)
  },
  {
    path: 'tenants',
    loadChildren: () => import('./features/tenants/tenants-module').then(m => m.TenantsModule)
  },
  {
    path: 'affiliates',
    loadChildren: () => import('./features/affiliates/affiliates-module').then(m => m.AffiliatesModule)
  },
  {
    path: 'campaigns',
    loadChildren: () => import('./features/campaigns/campaigns-module').then(m => m.CampaignsModule)
  },
  {
    path: 'items',
    loadChildren: () => import('./features/items/items-module').then(m => m.ItemsModule)
  },
  {
    path: 'links',
    loadChildren: () => import('./features/links/links-module').then(m => m.LinksModule)
  }
];
