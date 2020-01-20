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
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';

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
	RegisterComponent,
    LoginComponent,
	ProfileComponent,
    StadiumLayoutComponent
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
