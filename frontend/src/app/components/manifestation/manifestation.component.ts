import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';
import { Title } from '@angular/platform-browser';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { DatePipe } from '@angular/common';
import { Layout } from 'src/app/models/layout';
import { Location } from 'src/app/models/location.model';
import { LocationService } from 'src/app/services/location.service';
import { LayoutService } from 'src/app/services/layout.service';
import { Section } from 'src/app/models/section';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { ReservationDetailsService } from 'src/app/services/reservation-details.service';
import { ReservationDetailsRequest } from 'src/app/models/reservation-details-request';
import { ReservationDetails } from 'src/app/models/reservation-details';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Reservation } from 'src/app/models/reservation';
import { ReservationService } from 'src/app/services/reservation.service';
import { Subject } from 'rxjs';

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
  private takenReservationDetails: ReservationDetails[];
  private reservationDetails: ReservationDetails[] = [];
  private selectedColor = 'rgb(38, 212, 125)';
  private reserving: boolean = false;
  private notifyReservation: Subject<any> = new Subject();
  private notifyUpdateEdit: Subject<any> = new Subject();

  private layoutName: String;
  private displaySections: any[] = [];

  constructor(private route: ActivatedRoute,
    private utilityService: UtilityService,
    private titleService: Title,
    private manifestationService: ManifestationService,
    private locationService: LocationService,
    private layoutService: LayoutService,
    private reservationDetailsService: ReservationDetailsService,
    private authenticationService: AuthenticationService,
    private reservationService: ReservationService,
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

  retrieveSelectedSeatsEdit(event) {
    console.log(event);
  }

  retrieveSelectedNoSeatsEdit(event) {
    console.log(event);
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
        this.location = data;
        this.setUpLayout();
      },

      error => {
        console.log(error);
      }
    )
  }

  private setUpLayout() {
    this.layoutService.getById(this.location.layoutId).subscribe(
      data => {
        this.layout = <Layout> data;
        this.displaySections = this.utilityService.getDisplaySectionsForLayout(this.layout, this.manifestation.selectedSections);
        this.layoutName = this.layout.name;
        this.setUpReservationDetails();
      },

      error => {
        console.log(error);
      }
    )
  }

  private setUpReservationDetails() {
    var request = new ReservationDetailsRequest();
    var dateSelect = <HTMLSelectElement>document.getElementById('date-selection');
    request.manifestationId = this.manifestation.manifestationId;
    request.manifestationDayId = +dateSelect.value;

    this.reservationDetailsService.viewAllManifestationDetails(request).subscribe(
      data => {
        setTimeout(() => {
          this.notifyUpdateEdit.next({
            sectionId: -3,
            totalSelected: 20
          });
          this.notifyUpdateEdit.next({
            sectionId: -5,
            totalSelected: 20
          })
        }, 2000);
        
        this.takenReservationDetails = data;
        this.refreshSeats(request.manifestationDayId);

        var seats = document.getElementsByClassName('seat');
        for (var i = 0; i < seats.length; i++) {
          var seat = <HTMLElement> seats[i];
          for (var j = 0; j < data.length; j++) {
            if (data[j].manifestationSectionId == +seat.getAttribute('data-manifestation-section') &&
            data[j].row == +seat.getAttribute('data-seat-row') &&
            data[j].column == +seat.getAttribute('data-seat-column') &&
            data[j].manifestationDayId == +seat.getAttribute('data-manifestation-day')
            ) {
              seat.setAttribute('data-status', 'taken');
              seat.style.background = 'red';
            }
          }
        }

        this.refreshStandingSections();
      },
      error => {
        console.log(error);
      }
    )
  }

  manifestationDayChanged(value) {
    
    this.setUpReservationDetails();
  }

  refreshSeats(value) {
    var seats = document.getElementsByClassName('seat');
    for (var i = 0; i < seats.length; i++) {
      var selected = false;
      seats[i].setAttribute('data-manifestation-day', value);

      for (var j = 0; j < this.reservationDetails.length; j++) {
        if (!this.reservationDetails[j].isSeating) continue;
        if (this.reservationDetails[j].manifestationDayId == +seats[i].getAttribute('data-manifestation-day') &&
        this.reservationDetails[j].manifestationSectionId == +seats[i].getAttribute('data-manifestation-section') &&
        this.reservationDetails[j].row == +seats[i].getAttribute('data-seat-row') &&
        this.reservationDetails[j].column == +seats[i].getAttribute('data-seat-column')) {
          (<HTMLElement>seats[i]).style.background = this.selectedColor;
          seats[i].setAttribute('data-status', 'selected');
          selected = true;
          break;
        }
      }

      if (!selected) {
        seats[i].setAttribute('data-status', 'free');
        (<HTMLElement>seats[i]).style.background = 'white';
      }

      
      
    }
  }

  private matchManifestationSectionBySection(section: Section) {
    return this.utilityService.matchManifestationSectionBySection(this.manifestation.selectedSections, section);
  }

  sendSelectedSeats(event) {
    var resDet = <ReservationDetails> event;
    var isRemoved = false;

    if (resDet.isSeating) {
      for (var i = 0; i < this.reservationDetails.length; i++) {
        if (this.reservationDetails[i].manifestationDayId == resDet.manifestationDayId &&
          this.reservationDetails[i].manifestationSectionId == resDet.manifestationSectionId &&
          this.reservationDetails[i].row == resDet.row &&
          this.reservationDetails[i].column == resDet.column) {
            this.reservationDetails.splice(i, 1);
            isRemoved = true;
            break;
          }
      }

      if (!isRemoved) {
        this.reservationDetails.push(resDet);
      }

    }

    console.log(this.reservationDetails);
  }

  sendSelectedNoSeats(event) {
    if (event.status == 'remove') {
      for (var i = 0; i < this.reservationDetails.length; i++) {
        if (this.reservationDetails[i].manifestationDayId == event.manifestationDayId &&
          this.reservationDetails[i].manifestationSectionId == event.manifestationSectionId) {
            this.reservationDetails.splice(i, 1);
            break;
          }
      }
      
    }
    else if (event.status == 'add'){

      var manSection = this.getManifestationSectionById(event.manifestationSectionId);
      var taken = this.getTotalTakenSpaces(event.manifestationSectionId, event.manifestationDayId).length;
      var counter = this.getTotalForIdAndDay(event.manifestationSectionId, event.manifestationDayId).length;
      if (counter >= (manSection.size - taken)) return;

      var resDet = new ReservationDetails();
      resDet.manifestationDayId = event.manifestationDayId;
      resDet.manifestationSectionId = event.manifestationSectionId;
      resDet.isSeating = false;
      resDet.row = 0;
      resDet.column = 0;
      this.reservationDetails.push(resDet);
    }

    this.refreshStandingSections();

  }

  refreshStandingSections(): void {
    var sections = document.getElementsByClassName('current-selected-standing-section');
    var day = <HTMLSelectElement> document.getElementById('date-selection');
    for (var i = 0; i < sections.length; i++) {
      
      var id = +sections[i].getAttribute('data-manfestation-section');
      var manSection = this.getManifestationSectionById(id);
      var counter = this.getTotalForIdAndDay(id, +day.value).length;
      var taken = this.getTotalTakenSpaces(id, +day.value).length;
      

      (<HTMLElement>sections[i]).innerHTML = 'Selected: ' + counter.toString() + ' / ' + (manSection.size - taken).toString();

    }
  }

  getTotalTakenSpaces(manId: number, dayId): ReservationDetails[] {
    var taken = []
    for (var i = 0; i < this.takenReservationDetails.length; i++) {
      if (this.takenReservationDetails[i].manifestationDayId == dayId &&
        this.takenReservationDetails[i].manifestationSectionId == manId) {
          taken.push(this.takenReservationDetails[i]);
        }
    }
    return taken;
  }

  getTotalForIdAndDay(manId: number, dayId: number): ReservationDetails[] {
    var details = [];
    for (var j = 0; j < this.reservationDetails.length; j++) {
      if (this.reservationDetails[j].manifestationSectionId == manId &&
        this.reservationDetails[j].manifestationDayId == dayId) {
          details.push(this.reservationDetails[j]);
        }
    }
    return details;
  }

  getManifestationSectionById(id: number): ManifestationSection {
    for (var i = 0; i < this.displaySections.length; i++) {
      if (id == this.displaySections[i].manifestationSection.sectionId) {
        return this.displaySections[i].manifestationSection;
      }
    }
    return null;
  }

  getManifestationDayById(id: number): any {
    for (var i = 0; i < this.manifestationDays.length; i++) {
      if (id == this.manifestationDays[i].id) {
        return this.manifestationDays[i];
      }
    }
    return null;
  }

  getSectionById(id: number): Section {
    for (var i = 0; i < this.layout.sections.length; i++) {
      if (this.layout.sections[i].id == id) {
        return this.layout.sections[i];
      }
    }
    return null;
  }

  reserveManifestation(): void {
    if (this.reserving) return;
    this.reserving = true;
    var reservation = new Reservation();
    reservation.manifestationId = this.manifestation.manifestationId;
    reservation.reservationDetails = this.reservationDetails;
    
    this.reservationService.reserveManifestation(reservation).subscribe(
      data => {
        this.reservationDetails = [];
        this.notifyReservation.next();
        this.reserving = false;
        this.setUpReservationDetails();
        console.log(data);
      },
      error => {
        console.log(error);
        this.reserving = false;
      }
    )
  }

}
