import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Section } from 'src/app/models/section';
import { ReservationDetails } from 'src/app/models/reservation-details';
import { Subject } from 'rxjs';

@Component({
  selector: 'div [app-theater-layout]',
  templateUrl: './theater-layout.component.html',
  styleUrls: ['./theater-layout.component.css']
})
export class TheaterLayoutComponent implements OnInit {

  @Input() public isEditing: boolean = false;
  @Input() public displaySections: Section[] = [];
  @Input() reservation: Subject<any>;
  @Input() updateEdit: Subject<any>;

  private notifyReservation: Subject<any> = new Subject();
  private notifyUpdateEdit: Subject<any> = new Subject();

  @Output() notifySeatSelection: EventEmitter<ReservationDetails> = new EventEmitter<ReservationDetails>();
  @Output() notifyNoSeatsSelection: EventEmitter<any> = new EventEmitter<any>();

  //For editing purposes
  @Output() notifySeatsSelectionEdit: EventEmitter<any> = new EventEmitter<any>();
  @Output() notifyNoSeatsSelectionEdit: EventEmitter<any> = new EventEmitter<any>();


  constructor() { }

  ngOnInit() {
    if (this.isEditing) {
      this.updateEdit.subscribe(
        data => {
          this.notifyUpdateEdit.next(data);
        }
      );
    }
    else {
      this.reservation.subscribe(
        data => {
          this.notifyReservation.next();
        }
      );
    }
  }

  sendSelectedSeatsEdit(event): void {
    this.notifySeatsSelectionEdit.emit(event);
  }

  sendSelectedNoSeatsEdit(event): void {
    this.notifyNoSeatsSelectionEdit.emit(event);
  }

  sendSelectedSeats(event): void {
    this.notifySeatSelection.emit(event);
  }

  sendSelectedNoSeats(event): void {
    this.notifyNoSeatsSelection.emit(event);
  }
}
