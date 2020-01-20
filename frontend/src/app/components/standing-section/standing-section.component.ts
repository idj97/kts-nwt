import { Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef, Input, Output, EventEmitter } from '@angular/core';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { ReservationDetails } from 'src/app/models/reservation-details';

@Component({
  selector: 'div [app-standing-section]',
  templateUrl: './standing-section.component.html',
  styleUrls: ['./standing-section.component.css']
})
export class StandingSectionComponent implements OnInit {


  @ViewChild('sectionHolder', {static : false}) sectionHolder : ElementRef;
  @ViewChild('enableDisableIcon', {static : false}) enableDisableIcon : ElementRef;

  @Input() public isEditing: boolean;
  @Input() public totalSpace: number = 0;
  @Input() public manifestationSection: ManifestationSection;

  @Output() notifyNoSeatsSelection: EventEmitter<any> = new EventEmitter<any>();

  public totalSelected: number = 0;
  public userCurrentlySelected: number = 0;
  private isDisabled: boolean = false;
  private reservationDetails: ReservationDetails[] = [];


  constructor(private cdRef: ChangeDetectorRef) {
    
  }

  ngOnInit() {

  }

  ngAfterViewInit() {
    if (this.isEditing) {
      if (this.isDisabled) {
        this.disableSection();
      }
      else {
        this.enableSection();
      }
    }

    this.cdRef.detectChanges();
  }


  enableDisableSection(event) {
    if (!this.isEditing) return;
    
    if (!this.isDisabled) {
      this.disableSection();
    }
    else {
      this.enableSection();
    }

  }


  disableSection() {
    this.sectionHolder.nativeElement.style.opacity = '.5';
    this.enableDisableIcon.nativeElement.classList.remove('fa-close');
    this.enableDisableIcon.nativeElement.classList.add('fa-check');
    this.isDisabled = true;
  }

  enableSection() {
    this.sectionHolder.nativeElement.style.opacity = '1';
    this.enableDisableIcon.nativeElement.classList.add('fa-close');
    this.enableDisableIcon.nativeElement.classList.remove('fa-check');
    this.isDisabled = false;
  }


  addTotalSelected() {
    if (this.isDisabled) return;

    this.totalSelected++;
  }

  subtractTotalSelected() {
    if (this.isDisabled) return;
    if (this.totalSelected <= 0) return;

    this.totalSelected--;
  }

  addUserCurrentlySelected() {
    this.notifyNoSeatsSelection.emit(
      {
        manifestationSectionId : this.manifestationSection.sectionId,
        manifestationDayId: +(<HTMLSelectElement>document.getElementById('date-selection')).value,
        status: 'add'
      }
    );
  }

  subtractUserCurrentlySelected() {
    this.notifyNoSeatsSelection.emit(
      {
        manifestationSectionId : this.manifestationSection.sectionId,
        manifestationDayId: +(<HTMLSelectElement>document.getElementById('date-selection')).value,
        status: 'remove'
      }
    );
  }

}
