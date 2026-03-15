import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LinkGenerator } from './link-generator/link-generator';

const routes: Routes = [
  { path: '', component: LinkGenerator }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LinksRoutingModule { }
