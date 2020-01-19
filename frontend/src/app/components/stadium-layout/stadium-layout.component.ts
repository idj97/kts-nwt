import { Component, OnInit, HostListener, Input, Output, EventEmitter } from '@angular/core';
import { Section } from 'src/app/models/section';

@Component({
  selector: 'div [app-stadium-layout]',
  templateUrl: './stadium-layout.component.html',
  styleUrls: ['./stadium-layout.component.css']
})
export class StadiumLayoutComponent implements OnInit {

  @Input() public isEditing: boolean = false;
  @Input() public displaySections: Section[] = [];
  @Output() notifySeatSelection: EventEmitter<HTMLElement[]> = new EventEmitter<HTMLElement[]>();

  public selectedSeats: HTMLElement[] = [];

  constructor() { }

  ngOnInit() {
  }

  sendSelectedSeats(event): void {
    console.log(event);
    this.notifySeatSelection.emit(this.selectedSeats);
  }


}
