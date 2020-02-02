import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ManageManifestationComponent } from './components/manage-manifestation/manage-manifestation.component';
import { ManifestationsComponent } from './components/manifestations/manifestations.component';
import { ManifestationComponent } from './components/manifestation/manifestation.component';
import { CustomerReservationsComponent } from './components/customer-reservations/customer-reservations.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ManageAdminsComponent } from './components/manage-admins/manage-admins.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { CreateAdminComponent } from './components/create-admin/create-admin.component';
import { ReportsComponent } from './components/reports/reports.component';

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
    path : 'profile',
    component : ProfileComponent
  },
  {
    path : 'register',
    component : RegisterComponent
  },
  {
    path : 'login',
    component : LoginComponent
  },
  {

    path : 'manifestations',
    component : ManifestationsComponent
  },
  {
    path : 'manifestations/:sectionId',
    component : ManifestationComponent

  },
  {
    path: 'manage-manifestation', // used for creating a manifestation
    component: ManageManifestationComponent
  },
  {
    path: 'manage-manifestation/:sectionId', // used for editing a manifestation
    component: ManageManifestationComponent
  },
  {
    path: 'reservations',
    component: CustomerReservationsComponent
  },
  {
    path: 'manage-admins',
    component: ManageAdminsComponent
  },
  {
    path: 'manage-users',
    component: ManageUsersComponent
  },
  {
    path: 'create-admin',
    component: CreateAdminComponent
  },
  {
    path: 'reports',
    component: ReportsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
