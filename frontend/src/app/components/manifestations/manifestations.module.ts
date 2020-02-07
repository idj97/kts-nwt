import { NgModule } from '@angular/core';
import { ManifestationsComponent } from './manifestations.component';
import { ManifestationComponent } from '../manifestation/manifestation.component';
import { ManifestationItemComponent } from './manifestation-item/manifestation-item.component';
import { BrowserModule } from '@angular/platform-browser';
import { LayoutSectionsModule } from '../layout-sections/layout.sections.module';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { DlDateTimeDateModule, DlDateTimePickerModule } from 'angular-bootstrap-datetimepicker';
import { FormsModule } from '@angular/forms';

@NgModule({
    declarations:[
        ManifestationsComponent,
        ManifestationComponent,
        ManifestationItemComponent
    ],
    imports: [
        BrowserModule,
        LayoutSectionsModule, 
        AppRoutingModule,
        FormsModule,
        DlDateTimeDateModule,
        DlDateTimePickerModule,
    ],
    providers: []
})
export class ManifestationsModule {

}