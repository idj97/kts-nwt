import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Manifestation } from '../models/manifestation.model';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ManifestationService {

  constructor(private http: HttpClient) { }

  getAllManifestations(): Observable<Array<Manifestation>> {
    return this.http.get<Array<Manifestation>>(`${environment.baseUrl}/manifestation`);
  }

  createManifestation(manifestationData: Manifestation): Observable<Manifestation> {
    return this.http.post<Manifestation>(`${environment.baseUrl}/manifestation`, manifestationData);
  }

}
