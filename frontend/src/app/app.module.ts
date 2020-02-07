import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptors/token.interceptor';

import { AppComponent } from './app.component';
import { ManifestationItemComponent } from './components/manifestations/manifestation-item/manifestation-item.component';
import { ManageManifestationModule } from './components/manage-manifestation/manage-manifestation.module';
import { CustomerReservationsComponent } from './components/customer-reservations/customer-reservations.component';
import { ManageAdminsComponent } from './components/manage-admins/manage-admins.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { CreateAdminComponent } from './components/create-admin/create-admin.component';
import { ReportsComponent } from './components/reports/reports.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { DlDateTimeDateModule, DlDateTimePickerModule } from 'angular-bootstrap-datetimepicker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeModule } from './components/home/home.module';
import { ManifestationsModule } from './components/manifestations/manifestations.module';

@NgModule({
  declarations: [
    AppComponent,
    CustomerReservationsComponent,
    ManageAdminsComponent,
    ManageUsersComponent,
    CreateAdminComponent,
    ReportsComponent,
    PaginationComponent,
  ],
  entryComponents: [ManifestationItemComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    DlDateTimeDateModule,
    DlDateTimePickerModule,
    FormsModule,
    BrowserAnimationsModule
    ManageManifestationModule, // manifestation create and edit
    HomeModule, // home, login, register, update profile
    ManifestationsModule // search manifestations, view manifestation details
  ],
  providers: [
    FormsModule,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
