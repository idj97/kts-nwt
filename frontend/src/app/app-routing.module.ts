import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ManageManifestationComponent } from './components/manage-manifestation/manage-manifestation.component';

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
    path : 'login',
    component : LoginComponent
  },
  {
    path: 'manage-manifestation', // used for creating a manifestation
    component: ManageManifestationComponent
  },
  {
    path: 'manage-manifestation/:id', // used for editing a manifestation
    component: ManageManifestationComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
