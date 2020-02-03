import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SectionService {

  sectionSubject: Subject<any>;

  constructor() {
    this.sectionSubject = new Subject<any>();
  }

  addPreviousSections(sections: Array<any>) {
    this.sectionSubject.next(sections);
  }

  getPreviousSections(): Observable<any> {
    return this.sectionSubject.asObservable();
  }
}
