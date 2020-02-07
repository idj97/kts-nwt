import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ReservationService } from 'src/app/services/reservation.service';
import { UtilityService } from 'src/app/services/utility.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-customer-reservations',
  templateUrl: './customer-reservations.component.html',
  styleUrls: ['./customer-reservations.component.css'],
  providers: [DatePipe]
})
export class CustomerReservationsComponent implements OnInit, AfterViewInit {
  private reservations: any[] = [];

  constructor(
    private titleService: Title,
    private reservationService: ReservationService,
    private utilityService: UtilityService,
    private datePipe: DatePipe
  ) {
    titleService.setTitle('m-booking | Reservations');
  }

  ngOnInit() {
    this.utilityService.resetNavbar();
  }

  ngAfterViewInit() {
    this.reservationService.getCustomerReservations().subscribe(
      data => {
        this.reservations = data;
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  requestReservation(reservationId) {
    this.reservationService.requestReservation(reservationId).subscribe(
      data => {
        window.open((<any> data).approveUrl, '_blank');
        
        console.log(data);
      },
      error => {
        console.log(error);
      }
    )
  }

  cancelReservation(reservation: any): void {
    this.reservationService.cancelRervation(reservation.id).subscribe(data => {
      const index = this.reservations.indexOf(reservation);
      this.reservations.splice(index, 1);
    });
  }
}
