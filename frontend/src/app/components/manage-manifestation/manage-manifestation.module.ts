import { NgModule } from '@angular/core';
import { ManageManifestationComponent } from './manage-manifestation.component';
import { ManageManifestationDaysComponent } from './manage-manifestation-days/manage-manifestation-days.component';
import { ManageManifestationSectionsComponent } from './manage-manifestation-sections/manage-manifestation-sections.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { DlDateTimeDateModule, DlDateTimePickerModule } from 'angular-bootstrap-datetimepicker';
import { DatePipe } from '@angular/common';
import { ManageManifestationImagesComponent } from './manage-manifestation-images/manage-manifestation-images.component';

@NgModule({
    declarations: [
        ManageManifestationComponent,
        ManageManifestationDaysComponent,
        ManageManifestationSectionsComponent,
        ManageManifestationImagesComponent
    ],
    imports: [
        BrowserModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        DlDateTimeDateModule,
        DlDateTimePickerModule
    ],
    providers: [DatePipe]
})

export class ManageManifestationModule {

}