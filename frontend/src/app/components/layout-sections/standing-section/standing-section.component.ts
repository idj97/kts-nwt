import { Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef, Input, Output, EventEmitter } from '@angular/core';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { ReservationDetails } from 'src/app/models/reservation-details';
import { Section } from 'src/app/models/section';
import { Subject } from 'rxjs';

@Component({
  selector: 'div [app-standing-section]',
  templateUrl: './standing-section.component.html',
  styleUrls: ['./standing-section.component.css']
})
export class StandingSectionComponent implements OnInit {


  @ViewChild('sectionHolder', {static : false}) sectionHolder : ElementRef;
  @ViewChild('enableDisableIcon', {static : false}) enableDisableIcon : ElementRef;

  @Input() public section: Section;
  @Input() public isEditing: boolean;
  @Input() public totalSpace: number = 0;
  @Input() public manifestationSection: ManifestationSection;
  @Input() updateEdit: Subject<any>;

  @Output() notifyNoSeatsSelection: EventEmitter<any> = new EventEmitter<any>();
  @Output() notifyNoSeatsSelectionEdit: EventEmitter<any> = new EventEmitter<any>();

  public totalSelected: number = 0;
  public userCurrentlySelected: number = 0;
  private isDisabled: boolean = false;
  private ticketPrice: number;
  private reservationDetails: ReservationDetails[] = [];


  constructor(private cdRef: ChangeDetectorRef) {
    
  }

  ngOnInit() {
    this.updateEdit.subscribe(
      data => {
        if (data.sectionId == this.section.id) {
          this.totalSelected = data.totalSelected;
          this.ticketPrice = data.ticketPrice;
        }
      }
    );
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
    else {
      if (this.manifestationSection == null) {
        this.disableSection();
      }
    }

    this.cdRef.detectChanges();
  }

  sendSelectedSeatsEdit(): void {
    var editData = {
      sectionId: this.section.id,
      isSeating: true,
      isDisabled: this.isDisabled,
      totalSelected: this.totalSelected + 1,
      ticketPrice: this.ticketPrice
    }
    this.notifyNoSeatsSelectionEdit.emit(editData);
  }

  disableExponent(event: any) {
    return event.keyCode !== 69;
  }


  enableDisableSection(event) {
    if (!this.isEditing) return;
    
    if (!this.isDisabled) {
      this.disableSection();
    }
    else {
      this.enableSection();
    }
    this.sendSelectedSeatsEdit();
  }


  disableSection() {
    this.sectionHolder.nativeElement.style.opacity = '.5';
    if (this.enableDisableIcon != null) {
      this.enableDisableIcon.nativeElement.classList.remove('fa-close');
      this.enableDisableIcon.nativeElement.classList.add('fa-check');
    }
    
    this.isDisabled = true;
  }

  enableSection() {
    this.sectionHolder.nativeElement.style.opacity = '1';
    if (this.enableDisableIcon != null) {
      this.enableDisableIcon.nativeElement.classList.add('fa-close');
      this.enableDisableIcon.nativeElement.classList.remove('fa-check');
    }
    this.isDisabled = false;
  }


  addTotalSelected() {
    if (this.isDisabled) return;

    this.totalSelected++;
    this.sendSelectedSeatsEdit();
  }

  subtractTotalSelected() {
    if (this.isDisabled) return;
    if (this.totalSelected <= 0) return;

    this.totalSelected--;
    this.sendSelectedSeatsEdit();
  }

  addUserCurrentlySelected() {
    if (this.isDisabled) return;
    this.notifyNoSeatsSelection.emit(
      {
        manifestationSectionId : this.manifestationSection.sectionId,
        manifestationDayId: +(<HTMLSelectElement>document.getElementById('date-selection')).value,
        status: 'add'
      }
    );
  }

  subtractUserCurrentlySelected() {
    if (this.isDisabled) return;
    this.notifyNoSeatsSelection.emit(
      {
        manifestationSectionId : this.manifestationSection.sectionId,
        manifestationDayId: +(<HTMLSelectElement>document.getElementById('date-selection')).value,
        status: 'remove'
      }
    );
  }

}
