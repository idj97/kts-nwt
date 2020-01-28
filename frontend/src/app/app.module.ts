import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptors/token.interceptor';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { ManifestationsComponent } from './components/manifestations/manifestations.component';
import { ManifestationItemComponent } from './components/manifestations/manifestation-item/manifestation-item.component';
import { ManifestationComponent } from './components/manifestation/manifestation.component';
import { ManageManifestationModule } from './components/manage-manifestation/manage-manifestation.module';
import { SeatingSectionComponent } from './components/seating-section/seating-section.component';
import { StandingSectionComponent } from './components/standing-section/standing-section.component';
import { StadiumLayoutComponent } from './components/stadium-layout/stadium-layout.component';
import { TheaterLayoutComponent } from './components/theater-layout/theater-layout.component';
import { OpenSpaceLayoutComponent } from './components/open-space-layout/open-space-layout.component';
import { CustomerReservationsComponent } from './components/customer-reservations/customer-reservations.component';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ManageAdminsComponent } from './components/manage-admins/manage-admins.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { CreateAdminComponent } from './components/create-admin/create-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    ManifestationsComponent,
    ManifestationItemComponent,
    ManifestationComponent,
    SeatingSectionComponent,
    StandingSectionComponent,
    StadiumLayoutComponent,
    TheaterLayoutComponent,
    OpenSpaceLayoutComponent,
    CustomerReservationsComponent,
    LoginComponent,
    ProfileComponent,
    ManageAdminsComponent,
    ManageUsersComponent,
    CreateAdminComponent,
  ],
  entryComponents: [ManifestationItemComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    ManageManifestationModule
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
