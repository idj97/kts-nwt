import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservationDetails } from '../models/reservation-details';
import { ReservationDetailsRequest } from '../models/reservation-details-request';

@Injectable({
  providedIn: 'root'
})
export class ReservationDetailsService {

  constructor(private http: HttpClient) { }

  viewAllManifestationDetails(body: ReservationDetailsRequest): Observable<Array<ReservationDetails>> {
    return this.http.post<Array<ReservationDetails>>('api/reservations/viewAllManifestationDetails', body);
  }
}
