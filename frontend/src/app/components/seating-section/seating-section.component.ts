import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, ChangeDetectorRef, Input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'div [app-seating-section]',
  templateUrl: './seating-section.component.html',
  styleUrls: ['./seating-section.component.css']
})
export class SeatingSectionComponent implements OnInit {


  @ViewChild('sectionHolder', {static : false}) sectionHolder : ElementRef;
  @ViewChild('enableDisableIcon', {static : false}) enableDisableIcon : ElementRef;

  public column: number = 10;
  public row: number = 10;

  @Input() public isEditing: boolean;
  public totalSelected: number = -1;
  private isDisabled: boolean = false;
  private locked: boolean = false;


  private htmlSectionRowsAndColumns: SafeHtml;

  constructor(private cdRef: ChangeDetectorRef,
    private sanitizer: DomSanitizer) {
    
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


  lock() {
    this.locked = !this.locked;
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

    var el = <HTMLLIElement> event.target;

    this.totalSelected = +el.getAttribute('data-seat-number');

  }

  mouseOverSeat(event) {
    if (this.isEditing && !this.locked) {
      var el = <HTMLLIElement> event.target;
      var parent = el.closest('.section-holder');
      
      var seatHolders = parent.getElementsByClassName('seat');

      var flag = false;
      for (var i = 0; i < seatHolders.length; i++) {
        var currentEl = <HTMLElement> seatHolders[i];
        

        if (!flag) {
          currentEl.style.background = 'black';
        }
        
        if (el == currentEl) flag = true;
      }
    }
  }

  mouseLeaveSeat(event) {
    if (this.isEditing && !this.locked) {
      var el = <HTMLLIElement> event.target;
      var parent = el.closest('.section-holder');
      
      var seatHolders = parent.getElementsByClassName('seat');

      for (var i = 0; i < seatHolders.length; i++) {
        var currentEl = <HTMLElement> seatHolders[i];
        var number = +currentEl.getAttribute('data-seat-number');
        if (number > this.totalSelected) {
          currentEl.style.background = 'white';
        }
        
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
