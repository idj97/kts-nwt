import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../models/reservation';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private http: HttpClient) { }

  reserveManifestation(reservation: Reservation): Observable<any> {
    return this.http.post<Reservation>('/api/reservations/reserve', reservation);
  }

  getCustomerReservations(): Observable<Array<Reservation>> {
    return this.http.post<Array<Reservation>>('api/reservations/view', null);
  }

  cancelRervation(id: number): Observable<any> {
    return this.http.post<any>(`api/reservations/cancel/${id}`, null);
  }

  requestReservation(reservationId: number) {
    return this.http.post(`api/payments/${reservationId}/request_payment`, null);
  }

  buyReservation(reservationId: number, orderId: string) {
    return this.http.post(`api/payments/${reservationId}/${orderId}/execute_payment`, null);
  }
}
