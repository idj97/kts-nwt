import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
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
    ManageManifestationModule,
    HomeModule,
    ManifestationsModule
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
