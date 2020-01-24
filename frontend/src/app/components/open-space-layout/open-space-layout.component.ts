import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Section } from 'src/app/models/section';
import { ReservationDetails } from 'src/app/models/reservation-details';
import { Subject } from 'rxjs';

@Component({
  selector: 'div [app-open-space-layout]',
  templateUrl: './open-space-layout.component.html',
  styleUrls: ['./open-space-layout.component.css']
})
export class OpenSpaceLayoutComponent implements OnInit {

  @Input() public isEditing: boolean = false;
  @Input() public displaySections: Section[] = [];
  @Input() reservation: Subject<any>;
  private notifyReservation: Subject<any> = new Subject();
  @Output() notifySeatSelection: EventEmitter<ReservationDetails> = new EventEmitter<ReservationDetails>();
  @Output() notifyNoSeatsSelection: EventEmitter<any> = new EventEmitter<any>();


  constructor() { }

  ngOnInit() {
    this.reservation.subscribe(
      data => {
        this.notifyReservation.next();
      }
    )
  }

  sendSelectedSeats(event): void {
    this.notifySeatSelection.emit(event);
  }

  sendSelectedNoSeats(event): void {
    this.notifyNoSeatsSelection.emit(event);
  }

}