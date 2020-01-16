import { Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef, Input } from '@angular/core';

@Component({
  selector: 'div [app-standing-section]',
  templateUrl: './standing-section.component.html',
  styleUrls: ['./standing-section.component.css']
})
export class StandingSectionComponent implements OnInit {


  @ViewChild('sectionHolder', {static : false}) sectionHolder : ElementRef;
  @ViewChild('enableDisableIcon', {static : false}) enableDisableIcon : ElementRef;

  @Input() public isEditing: boolean;
  private isDisabled: boolean = false;


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

}
