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
}
