import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Location } from '../../app/models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  constructor(private http: HttpClient) { }

  getAllLocations(): Observable<Array<Location>> {
    return this.http.get<Array<Location>>('api/locations/all');
  }

  getById(id: number): Observable<Location> {
    return this.http.get<Location>(`api/locations/${id}`);
  }

}
