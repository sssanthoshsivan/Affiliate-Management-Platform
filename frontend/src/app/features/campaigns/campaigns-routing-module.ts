import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CampaignList } from './campaign-list/campaign-list';

const routes: Routes = [
  { path: '', component: CampaignList }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CampaignsRoutingModule { }
