import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, ChangeDetectorRef, Input, HostListener, Output, EventEmitter } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'div [app-seating-section]',
  templateUrl: './seating-section.component.html',
  styleUrls: ['./seating-section.component.css']
})
export class SeatingSectionComponent implements OnInit {


  @ViewChild('sectionHolder', {static : false}) sectionHolder : ElementRef;
  @ViewChild('enableDisableIcon', {static : false}) enableDisableIcon : ElementRef;

  @Input() public column: number = 10;
  @Input() public row: number = 10;
  @Input() public isEditing: boolean;

  @Output() notifySeatSelection: EventEmitter<HTMLElement[]> = new EventEmitter<HTMLElement[]>();

  public totalSelected: number = -1;
  private isDisabled: boolean = false;

  private htmlSectionRowsAndColumns: SafeHtml;

  private selectedColor = 'rgb(38, 212, 125)';
  private takenColor = 'red';
  private notSelectedColor = 'white';

  public selectedSeats: HTMLElement[] = [];


  constructor(private cdRef: ChangeDetectorRef,
    private sanitizer: DomSanitizer) {
    
  }

  sendSelectedSeats(): void {
    this.notifySeatSelection.emit(this.selectedSeats);
  }

  ngOnInit() {
    
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
    this.sectionHolder.nativeElement.style.width = this.column * 25 + 'px';
    var html = '';
    var counter = 0;
    for (var i = 0; i < this.row; i++) {
      html +='<tr class="seat-holder">';
      for (var j = 0; j < this.column; j++) {
        html += `<td><div class='seat' data-seat-row=${i} data-seat-column=${j} data-seat-number=${counter}></div></td>`;
        counter++;
      }
      html += '</tr>'
    }
    
    this.htmlSectionRowsAndColumns = this.sanitizer.bypassSecurityTrustHtml(html);
    setTimeout(() => {
      var els = this.sectionHolder.nativeElement.getElementsByClassName('seat');
      for (var i = 0; i < els.length; i++) {
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
    }
    else {
      var index = this.selectedSeats.indexOf(el);
      if (index == -1) {
        this.selectedSeats.push(el);
        el.style.background = this.selectedColor;
      }
      else {
        this.selectedSeats.splice(index, 1);
        el.style.background = this.notSelectedColor;
      }

      this.sendSelectedSeats();
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
      if (this.selectedSeats.indexOf(el) == -1)
        el.style.background = this.selectedColor;
    }
  }

  mouseLeaveSeat(event) {
    if (this.isDisabled) return;
    var el = <HTMLLIElement> event.target;
    if (this.isEditing) {
      var parent = el.closest('.section-holder');
      
      var seatHolders = parent.getElementsByClassName('seat');

      for (var i = 0; i < seatHolders.length; i++) {
        var currentEl = <HTMLElement> seatHolders[i];
        var number = +currentEl.getAttribute('data-seat-number');
        if (number > this.totalSelected) {
          currentEl.style.background = this.notSelectedColor;
        }
        
      }
    }
    else {
      if (this.selectedSeats.indexOf(el) == -1)
        el.style.background = this.notSelectedColor;
    }
  }

  addTotalSelected() {
    if (this.isDisabled) return;
    if (this.totalSelected >= this.column * this.row - 1) return;
    this.totalSelected++;
    this.colorizeSeats();
  }

  subtractTotalSelected() {
    if (this.isDisabled) return;
    if (this.totalSelected <= -1) return;
    this.totalSelected--;
    this.colorizeSeats();
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

}
