import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AffiliateList } from './affiliate-list/affiliate-list';

const routes: Routes = [
  { path: '', component: AffiliateList }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AffiliatesRoutingModule { }
