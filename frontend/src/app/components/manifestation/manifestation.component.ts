import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';
import { Title } from '@angular/platform-browser';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { DatePipe } from '@angular/common';
import { Layout } from 'src/app/models/layout';
import { LocationService } from 'src/app/services/location.service';
import { LayoutService } from 'src/app/services/layout.service';
import { Section } from 'src/app/models/section';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';

@Component({
  selector: 'app-manifestation',
  templateUrl: './manifestation.component.html',
  styleUrls: ['./manifestation.component.css'],
  providers: [DatePipe]
})
export class ManifestationComponent implements OnInit {

  private id: Number;

  private name: String;
  private description: String;
  private date: String;
  private selectedSeats: HTMLElement[] = [];
  private manifestation: Manifestation;
  private manifestationDays: any[] = [];
  private location: Location;
  private layout: Layout;

  private layoutName: String;
  private displaySections: any[] = [];

  constructor(private route: ActivatedRoute,
    private utilityService: UtilityService,
    private titleService: Title,
    private manifestationService: ManifestationService,
    private locationService: LocationService,
    private layoutService: LayoutService,
    private datePipe: DatePipe) {

      this.route.params.subscribe(
        params => {
          this.id = params['id'];
        }
      )


      this.utilityService.setNavbar();
  }

  ngOnInit() {
    this.setUpManifestation();
  }

  @HostListener('window:scroll', ['$event'])
  scrolled(event) {
    this.utilityService.setNavbar();
  }


  private setUpManifestation() {
    this.manifestationService.getManifestationById(this.id).subscribe(
      data => {
        var man = <Manifestation> data;
        this.manifestation = man;
        for (var i = 0; i < this.manifestation.manifestationDates.length; i++) {
          this.manifestationDays.push({
            id: this.manifestation.manifestationDaysId[i],
            date: this.datePipe.transform(this.manifestation.manifestationDates[i], 'dd.MM.yyyy HH:mm')
          });
        }
        

        this.name = man.name;
        this.description = man.description;
        this.date = this.datePipe.transform(man.reservableUntil, 'dd.MM.yyyy HH:mm');

        this.titleService.setTitle('m-booking | ' + this.name);
        this.setUpLocation();
      },

      error => {
        console.log(error);
      }
    )
  }

  private setUpLocation() {
    this.locationService.getById(this.manifestation.locationId).subscribe(
      data => {
        this.location = <Location> data;
        console.log(this.location);
        this.setUpLayout();
      },

      error => {
        console.log(error);
      }
    )
  }

  private setUpLayout() {
    this.layoutService.getById(this.manifestation.locationId).subscribe(
      data => {
        this.layout = <Layout> data;
        
        if (this.layout.name == "STADIUM") {
          for (var i = 0; i < this.layout.sections.length; i++) {
            if (this.layout.sections[i].name == "PARTER") {
              this.displaySections[0] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "NORTH") {
              this.displaySections[1] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "EAST") {
              this.displaySections[2] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "SOUTH") {
              this.displaySections[3] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "WEST") {
              this.displaySections[4] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
          }
        }

        if (this.layout.name == "THEATER") {
          for (var i = 0; i < this.layout.sections.length; i++) {
            if (this.layout.sections[i].name == "CLASS_1") {
              this.displaySections[0] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "CLASS_2") {
              this.displaySections[1] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "CLASS_3") {
              this.displaySections[2] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "CLASS_4") {
              this.displaySections[3] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
          }
        }

        if (this.layout.name == "OPEN_SPACE") {
          for (var i = 0; i < this.layout.sections.length; i++) {
            if (this.layout.sections[i].name == "AREA_1") {
              this.displaySections[0] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "AREA_2") {
              this.displaySections[1] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "AREA_3") {
              this.displaySections[2] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
            else if (this.layout.sections[i].name == "AREA_4") {
              this.displaySections[3] = {
                section: this.layout.sections[i],
                manifestationSection: this.matchManifestationSectionBySection(this.layout.sections[i])
              };
            }
          }
        }
        
        this.layoutName = this.layout.name;
        console.log(this.layout);
        console.log(this.displaySections);
      },

      error => {
        console.log(error);
      }
    )
  }

  private matchManifestationSectionBySection(section: Section) {
    for (var i = 0; i < this.manifestation.selectedSections.length; i++) {
      if (this.manifestation.selectedSections[i].selectedSectionId == section.id) {
        return this.manifestation.selectedSections[i];
      }
    }

    return null;
  }

}
