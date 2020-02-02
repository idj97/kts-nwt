import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, ChangeDetectorRef, Input, HostListener, Output, EventEmitter } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { Section } from 'src/app/models/section';
import { ReservationDetails } from 'src/app/models/reservation-details';
import { Subject } from 'rxjs';

@Component({
  selector: 'div [app-seating-section]',
  templateUrl: './seating-section.component.html',
  styleUrls: ['./seating-section.component.css']
})

export class SeatingSectionComponent implements OnInit {


  @ViewChild('sectionHolder', {static : false}) sectionHolder : ElementRef;
  @ViewChild('enableDisableIcon', {static : false}) enableDisableIcon : ElementRef;

  //@Input() public column: number = 10;
  //@Input() public row: number = 10;
  @Input() public section: Section;
  @Input() public isEditing: boolean;
  @Input() public manifestationSection: ManifestationSection;
  @Input() reservation: Subject<any> = new Subject<any>();
  @Input() updateEdit: Subject<any> = new Subject<any>();

  @Output() notifySeatSelection: EventEmitter<ReservationDetails> = new EventEmitter<ReservationDetails>();
  @Output() notifySeatsSelectionEdit: EventEmitter<any> = new EventEmitter<any>();

  public totalSelected: number = -1;
  private isDisabled: boolean = false;

  private htmlSectionRowsAndColumns: SafeHtml;

  private selectedColor = 'rgb(38, 212, 125)';
  private takenColor = 'red';
  private notSelectedColor = 'white';

  private selectedSeats: ReservationDetails[] = [];


  constructor(private cdRef: ChangeDetectorRef,
    private sanitizer: DomSanitizer) {
    
  }

  sendSelectedSeatsEdit(): void {
    var editData = {
      sectionId: this.section.id,
      isSeating: true,
      isDisabled: this.isDisabled,
      totalSelected: this.totalSelected + 1
    }
    this.notifySeatsSelectionEdit.emit(editData);
  }

  sendSelectedSeats(reservationDetails): void {
    this.notifySeatSelection.emit(reservationDetails);
  }

  ngOnInit() {
    if (this.isEditing) {
      this.updateEdit.subscribe(
        data => {
          console.log(data);
          if (data.sectionId == this.section.id) {
            this.totalSelected = data.totalSelected;
            this.colorizeSeats();
          }
        }
      );
    }
    else {
      this.reservation.subscribe(
        data => {
          this.selectedSeats = [];
        }
      );
    }
  }

  ngAfterViewInit() {
    this.generateRowsAndColumns();
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

  generateRowsAndColumns() {
    this.sectionHolder.nativeElement.style.width = this.section.sectionColumns * 25 + 'px';
    var html = '';
    var counter = 0;
    for (var i = 0; i < this.section.sectionRows; i++) {
      html +='<tr class="seat-holder">';
      for (var j = 0; j < this.section.sectionColumns; j++) {
        if (this.isEditing) {
          html += `<td><div class='seat' data-seat-row=${i} data-seat-column=${j} data-seat-number=${counter}></div></td>`;
        }
        else {
          if (this.manifestationSection != null && counter < this.manifestationSection.size ) {
            var select = <HTMLSelectElement>document.getElementById('date-selection');
            html += `<td><div class='seat' data-seat-row=${i} data-seat-column=${j} data-seat-number=${counter} data-manifestation-section=${this.manifestationSection.id} data-section=${this.manifestationSection.selectedSectionId} data-manifestation-day=${select.value} data-status='free'></div></td>`;
          }
          else {
            html += `<td><div class='seat disabled-seat' data-seat-row=${i} data-seat-column=${j} data-seat-number=${counter}></div></td>`;
          }
          
        }
        
        counter++;
      }
      html += '</tr>';
    }
    
    this.htmlSectionRowsAndColumns = this.sanitizer.bypassSecurityTrustHtml(html);
    setTimeout(() => {
      var els = this.sectionHolder.nativeElement.getElementsByClassName('seat');
      for (var i = 0; i < els.length; i++) {
        if (els[i].classList.contains("disabled-seat")) continue;
        els[i].addEventListener("mouseenter", this.mouseOverSeat.bind(this), false);
        els[i].addEventListener("mouseleave", this.mouseLeaveSeat.bind(this), false);
        els[i].addEventListener("click", this.clickSeat.bind(this), false);
      }
    }, 100);
  }

  clickSeat(event) {
    if (this.isDisabled) return;

    var el = <HTMLElement> event.target;

    if (this.isEditing) {
      this.totalSelected = +el.getAttribute('data-seat-number');
      this.sendSelectedSeatsEdit();
    }
    else {
      if (el.getAttribute('data-status') == 'taken') return;
      var data = this.isSelected(el);
      if (!data.status) {
        var rd = new ReservationDetails();
        rd.manifestationSectionId = +el.getAttribute('data-manifestation-section');
        rd.manifestationDayId = +el.getAttribute('data-manifestation-day');
        rd.row = +el.getAttribute('data-seat-row');
        rd.column = +el.getAttribute('data-seat-column');
        this.selectedSeats.push(rd);
        el.style.background = this.selectedColor;
        el.setAttribute('data-status', 'selected');
      }
      else {
        this.selectedSeats.splice(data.index, 1);
        el.setAttribute('data-status', 'free');
        el.style.background = this.notSelectedColor;
      }

      var reservationDetails = new ReservationDetails();
      reservationDetails.manifestationDayId = +el.getAttribute('data-manifestation-day');
      reservationDetails.manifestationSectionId = +el.getAttribute('data-manifestation-section');
      reservationDetails.row = +el.getAttribute('data-seat-row');
      reservationDetails.column = +el.getAttribute('data-seat-column');
      reservationDetails.isSeating = true;

      this.sendSelectedSeats(reservationDetails);
    }

  }

  mouseOverSeat(event) {
    if (this.isDisabled) return;
    var el = <HTMLLIElement> event.target;
    if (this.isEditing) {
      var parent = el.closest('.section-holder');
      
      var seatHolders = parent.getElementsByClassName('seat');

      var flag = false;
      for (var i = 0; i < seatHolders.length; i++) {
        var currentEl = <HTMLElement> seatHolders[i];
        

        if (!flag) {
          currentEl.style.background = this.selectedColor;
        }
        
        if (el == currentEl) flag = true;
      }
    }
    else {
      if (el.getAttribute('data-status') == 'taken') return;
      var data = this.isSelected(el);
      if (!data.status)
        el.style.background = this.selectedColor;
    }
  }

  mouseLeaveSeat(event) {
    if (this.isDisabled) return;
    var el = <HTMLLIElement> event.target;
    if (this.isEditing) {
      this.colorizeSeats();
    }
    else {
      if (el.getAttribute('data-status') == 'taken') return;
      var data = this.isSelected(el);
      if (!data.status)
        el.style.background = this.notSelectedColor;
    }
  }

  addTotalSelected() {
    if (this.isDisabled) return;
    if (this.totalSelected >= this.section.sectionColumns * this.section.sectionRows - 1) return;
    this.totalSelected++;
    this.colorizeSeats();
    this.sendSelectedSeatsEdit();
  }

  subtractTotalSelected() {
    if (this.isDisabled) return;
    if (this.totalSelected <= -1) return;
    this.totalSelected--;
    this.colorizeSeats();
    this.sendSelectedSeatsEdit();
  }

  colorizeSeats() {
    var seatHolders = this.sectionHolder.nativeElement.getElementsByClassName('seat');

    for (var i = 0; i < seatHolders.length; i++) {
      var currentEl = <HTMLElement> seatHolders[i];
      var number = +currentEl.getAttribute('data-seat-number');
      if (number > this.totalSelected) {
        currentEl.style.background = this.notSelectedColor;
      }
      else {
        currentEl.style.background = this.selectedColor;
      }
      
    }
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

  isSelected(el: HTMLElement): any {
    for (var i = 0; i < this.selectedSeats.length; i++) {
      if (this.selectedSeats[i].row == +el.getAttribute('data-seat-row') &&
      this.selectedSeats[i].column == +el.getAttribute('data-seat-column') &&
      this.selectedSeats[i].manifestationDayId == +el.getAttribute('data-manifestation-day') &&
      this.selectedSeats[i].manifestationSectionId == +el.getAttribute('data-manifestation-section')) {
        return {
          index: i,
          status: true
        };
      }
    }

    return {
      index: null,
      status: false
    };;
  }

}
