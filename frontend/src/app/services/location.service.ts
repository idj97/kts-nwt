import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Location } from '../../app/models/location.model';
import { Results } from '../models/results';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  constructor(private http: HttpClient) { }

  getAllLocations(): Observable<Array<Location>> {
    return this.http.get<Array<Location>>('api/locations/all');
  }

  getById(id: number): Observable<Location>{
    return this.http.get<Location>(`api/locations/${id}`);
  }

  searchLocations(name = '', address = '', pageNum = 0, pageSize = 5): Observable<Results> {
    let params = new HttpParams();
    params = params.append('name', name);
    params = params.append('address', address);
    params = params.append('pageNum', pageNum.toString());
    params = params.append('pageSize', pageSize.toString());
    return this.http.get<Results>('api/locations', {params});
  }

  createLocation(location: Location) {
    return this.http.post<Location>('api/locations', location);
  }

  updateLocation(location: Location) {
    return this.http.put<Location>(`api/locations/${location.id}`, location);
  }

  deleteLocation(locationId: number) {
    return this.http.delete(`api/locations/${locationId}`);
  }


}
