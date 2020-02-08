import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Layout } from '../models/layout';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  constructor(private http: HttpClient) {

  }

  getById(id: number): Observable<Layout> {
    return this.http.get<Layout>(`api/layouts/${id}`);
  }

  getByName(name: string): Observable<Layout> {
    return this.http.get<Layout>(`api/layouts?name=${name}`);
  }

}
