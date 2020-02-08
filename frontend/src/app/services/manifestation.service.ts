import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Manifestation } from '../models/manifestation.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ManifestationService {

  constructor(private http: HttpClient) { }

  getAllManifestations(): Observable<Array<Manifestation>> {
    return this.http.get<Array<Manifestation>>('api/manifestation');
  }

  searchManifestations(searchData: any): Observable<Array<Manifestation>> {
    return this.http.get<Array<Manifestation>>(`api/manifestation/search?name=${searchData.name}&type=${searchData.type}&locationName=${searchData.locationName}&date=${searchData.date}`);
  }

  getManifestationById(id): Observable<Manifestation> {
    return this.http.get<Manifestation>(`api/manifestation/${id}`);
  }

  createManifestation(manifestationData: any): Observable<Manifestation> {
    return this.http.post<Manifestation>('api/manifestation', manifestationData);
  }

  updateManifestation(manifestationData: Manifestation): Observable<Manifestation> {
    return this.http.put<Manifestation>('api/manifestation', manifestationData);
  }

  uploadImages(data: FormData, manifestationId: number): Observable<any> {
    return this.http.post(`api/manifestation/upload/${manifestationId}`, data);
  }

  getReports(manifestationId: number): Observable<any> {
    return this.http.get(`/api/manifestation/reports/${manifestationId}`);
  }

}
