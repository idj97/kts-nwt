import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { ManageManifestationComponent } from './manage-manifestation/manage-manifestation.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path : 'home',
    component : HomeComponent
  },
  {
    path : 'register',
    component : RegisterComponent
  },
  {
    path: 'manage-manifestation', //used for creating a manifestation
    component: ManageManifestationComponent
  },
  {
    path: 'manage-manifestation/:id', //used for editing a manifestation
    component: ManageManifestationComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
