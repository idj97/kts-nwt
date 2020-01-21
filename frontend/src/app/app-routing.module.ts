import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { ManageManifestationComponent } from './components/manage-manifestation/manage-manifestation.component';
import { ManifestationsComponent } from './components/manifestations/manifestations.component';
import { ManifestationComponent } from './components/manifestation/manifestation.component';
import { CustomerReservationsComponent } from './components/customer-reservations/customer-reservations.component';

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
    path : 'manifestations',
    component : ManifestationsComponent
  },
  {
    path : 'manifestations/:id',
    component : ManifestationComponent
  },
  {
    path: 'manage-manifestation', // used for creating a manifestation
    component: ManageManifestationComponent
  },
  {
    path: 'manage-manifestation/:id', // used for editing a manifestation
    component: ManageManifestationComponent
  },
  {
    path: 'reservations',
    component: CustomerReservationsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
