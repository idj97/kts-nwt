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
import { ManageLocationsComponent } from './components/manage-locations/manage-locations.component';
import { CreateLocationComponent } from './components/create-location/create-location.component';
import { UpdateLocationComponent } from './components/update-location/update-location.component';
import { RoleGuard } from './guards/role.guard';
import { environment } from 'src/environments/environment';
import { LoginGuard } from './guards/login.guard';

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
    component : ProfileComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleCustomer, environment.roleAdmin, environment.roleSysAdmin]}
  },
  {
    path : 'register',
    component : RegisterComponent,
    canActivate: [LoginGuard]
  },
  {
    path : 'login',
    component : LoginComponent,
    canActivate: [LoginGuard]
  },
  {
    path : 'manifestations',
    component : ManifestationsComponent
  },
  {
    path : 'manifestations/:id',
    component : ManifestationComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleCustomer]}
  },
  {
    path: 'manage-manifestation', // used for creating a manifestation
    component: ManageManifestationComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleAdmin]}
  },
  {
    path: 'manage-manifestation/:id', // used for editing a manifestation
    component: ManageManifestationComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleAdmin]}
  },
  {
    path: 'reservations',
    component: CustomerReservationsComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleCustomer]}
  },
  {
    path: 'manage-admins',
    component: ManageAdminsComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleSysAdmin]}
  },
  {
    path: 'manage-users',
    component: ManageUsersComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleSysAdmin]}
  },
  {
    path: 'create-admin',
    component: CreateAdminComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleSysAdmin]}
  },
  {
    path: 'reports',
    component: ReportsComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleSysAdmin]}
  },

  {
    path: 'manage-locations',
    component: ManageLocationsComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleAdmin]}
  },
  {
    path: 'manage-locations/create',
    component: CreateLocationComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleAdmin]}
  },
  {
    path: 'manage-locations/update/:id',
    component: UpdateLocationComponent,
    canActivate: [RoleGuard],
    data: {permissions: [environment.roleAdmin]}
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
