import { NgModule } from "@angular/core";
import { OpenSpaceLayoutComponent } from './open-space-layout/open-space-layout.component';
import { SeatingSectionComponent } from './seating-section/seating-section.component';
import { StandingSectionComponent } from './standing-section/standing-section.component';
import { StadiumLayoutComponent } from './stadium-layout/stadium-layout.component';
import { TheaterLayoutComponent } from './theater-layout/theater-layout.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

@NgModule({
    declarations:[
        OpenSpaceLayoutComponent,
        StadiumLayoutComponent,
        TheaterLayoutComponent,
        SeatingSectionComponent,
        StandingSectionComponent
    ],
    imports: [
        BrowserModule,
        FormsModule
    ],
    providers:[

    ],
    exports: [
        OpenSpaceLayoutComponent,
        StadiumLayoutComponent,
        TheaterLayoutComponent,
        SeatingSectionComponent,
        StandingSectionComponent
    ]
})

export class LayoutSectionsModule {

}