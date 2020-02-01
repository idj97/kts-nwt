import { NgModule } from '@angular/core';
import { HomeComponent } from './home.component';
import { LoginComponent } from '../login/login.component';
import { RegisterComponent } from '../register/register.component';
import { BrowserModule } from '@angular/platform-browser';
import { ProfileComponent } from '../profile/profile.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ManifestationsModule } from '../manifestations/manifestations.module';
import { AppRoutingModule } from 'src/app/app-routing.module';

@NgModule({
    declarations: [
        HomeComponent,
        LoginComponent,
        RegisterComponent,
        ProfileComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        ManifestationsModule
    ],
    providers: []
})

export class HomeModule {

}