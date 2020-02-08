import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptors/token.interceptor';

import { AppComponent } from './app.component';
import { ManageManifestationModule } from './components/manage-manifestation/manage-manifestation.module';
import { CustomerReservationsComponent } from './components/customer-reservations/customer-reservations.component';
import { ManageAdminsComponent } from './components/manage-admins/manage-admins.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { CreateAdminComponent } from './components/create-admin/create-admin.component';
import { ReportsComponent } from './components/reports/reports.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeModule } from './components/home/home.module';
import { ManifestationsModule } from './components/manifestations/manifestations.module';
import { ChartsModule } from 'ng2-charts';
import { ManageLocationsComponent } from './components/manage-locations/manage-locations.component';
import { CreateLocationComponent } from './components/create-location/create-location.component';
import { UpdateLocationComponent } from './components/update-location/update-location.component';


@NgModule({
  declarations: [
    AppComponent,
    CustomerReservationsComponent,
    ManageAdminsComponent,
    ManageUsersComponent,
    CreateAdminComponent,
    ReportsComponent,
    PaginationComponent,
    ManageLocationsComponent,
    CreateLocationComponent,
    UpdateLocationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    ChartsModule,
    BrowserAnimationsModule,
    ManageManifestationModule, // manifestation create and edit
    HomeModule, // home, login, register, update profile
    ManifestationsModule, // search manifestations, view manifestation details
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
