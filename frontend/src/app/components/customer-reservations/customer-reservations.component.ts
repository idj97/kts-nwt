import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ReservationService } from 'src/app/services/reservation.service';
import { UtilityService } from 'src/app/services/utility.service';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

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
    private datePipe: DatePipe,
    private router: Router,
    private acitavtedRoute: ActivatedRoute
  ) {
    titleService.setTitle('m-booking | Reservations');
  }

  ngOnInit() {
    this.utilityService.resetNavbar();
    this.acitavtedRoute.queryParams.subscribe(
      params => {
        console.log(params['token']);
        if (params['token']) {
          this.reservationService.buyReservation(parseInt(window.localStorage.getItem('reservationId')), params['token']).subscribe(
            data => {
              this.getCustomerReservations();
              console.log(data);
            },
            error => {
              this.getCustomerReservations();
              console.log(error);
            }
          );
        }
        else {
          this.getCustomerReservations();
        }
        
      }
    )
  }

  getCustomerReservations() {
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

  ngAfterViewInit() {
    
  }

  requestReservation(reservationId) {
    this.reservationService.requestReservation(reservationId).subscribe(
      data => {
        const paymentRequest = <any> data
        window.localStorage.setItem('reservationId', reservationId);
        window.localStorage.setItem('orderId', paymentRequest.id);
        window.open((<any> paymentRequest).approveUrl, '_blank');
        
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
