import { Component, OnInit, ViewChild, ViewContainerRef, AfterViewInit, ComponentFactoryResolver, Type, HostListener, ElementRef, Sanitizer } from '@angular/core';
import { UtilityService } from 'src/app/services/utility.service';
import { Title, DomSanitizer } from '@angular/platform-browser';
import { ManifestationItemComponent } from './manifestation-item/manifestation-item.component';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs';
import { DateButton } from 'angular-bootstrap-datetimepicker';
import { ActivatedRoute } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-manifestations',
  templateUrl: './manifestations.component.html',
  styleUrls: ['./manifestations.component.css'],
  providers: [DatePipe],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: '0' }),
        animate('.5s ease-out', style({ opacity: '1' })),
      ]),
    ]),
  ],
})
export class ManifestationsComponent implements OnInit {

  //@ViewChild('container', {static: true, read: ViewContainerRef}) container: ViewContainerRef;

  @ViewChild('searchName', {static: false}) searchName: ElementRef;
  @ViewChild('searchDate', {static: false}) searchDate: ElementRef;
  @ViewChild('searchType', {static: false}) searchType: ElementRef;
  @ViewChild('searchLocation', {static: false}) searchLocation: ElementRef;
  @ViewChild('datepicker', {static: false}) datepicker: ElementRef;

  selectedDate: Date;
  popUpWidth: string;
  daysSubscription: Subscription;

  private manifestations: Manifestation[];
  private searchTimeout: any;
  private blurTimeout;
  private datepickerTimeout;
  private manifestationsRequested: boolean = false;

  private type: string;

  constructor(private utilityService: UtilityService,
    private titleService: Title,
    private componentFactoryResolver: ComponentFactoryResolver,
    private manifestationService: ManifestationService,
    private datepipe: DatePipe,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer) { 

      this.titleService.setTitle("m-booking | Manifestations");
      
    }

  ngOnInit() {
    this.utilityService.resetNavbar();
    document.getElementById("navbar").style.boxShadow = "none";
    document.getElementById("navbar").style.borderBottom = "2px solid black";

  }
  

  futureDatesOnly(dateButton: DateButton, viewName: string) {
    return dateButton.value > (new Date()).getTime();
  }

  getSelectedDate(): string {
    if (this.selectedDate == null) return "";
    return this.datepipe.transform(this.selectedDate, 'yyyy-MM-dd');
  }

  ngAfterViewInit() {
      this.route.queryParams.subscribe(
        params => {
          this.type = params['type'] || 'default';
          if (this.type == 'SPORT') {
            this.searchType.nativeElement.selectedIndex = '1';
          }
          else if (this.type == 'CULTURE') {
            this.searchType.nativeElement.selectedIndex = '2';
          }
          else if (this.type == 'ENTERTAINMENT') {
            this.searchType.nativeElement.selectedIndex = '3';
          }
          
          this.setUpManifestations();
        }
      )
  }

  sanitizeURL(url) {
    this.sanitizer.bypassSecurityTrustStyle(url);
  }

  inputChanged(): void {

    if (this.manifestationsRequested) return;
    this.manifestationsRequested = true;

    //this.container.clear();
    this.searchTimeout = setTimeout(() => {
      this.setUpManifestations();
    }, 200);
  }

  private setUpManifestations() {
    var searchData = {
      name: this.searchName.nativeElement.value,
      type: this.searchType.nativeElement.value,
      locationName: this.searchLocation.nativeElement.value,
      date: this.searchDate.nativeElement.value
    }

    this.manifestationService.searchManifestations(searchData).subscribe(
      data => {
        //Display manifestations
        this.manifestations = data;
        console.log(this.manifestations);
        this.manifestationsRequested = false;
      },
      error => {
        this.manifestationsRequested = false;
        console.log(error.error);
      }
    )
  }

  focusInput(event: FocusEvent) {
    var el = event.target;
    var parent = (<HTMLElement> el).parentElement;
    var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

    text.style.top = "-6px";
    text.style.fontSize = "12px";
    text.style.color = "darkcyan";
  }

  blurInput(event: FocusEvent) {
    clearTimeout(this.blurTimeout);
    this.blurTimeout = setTimeout(() => {
      var el = <HTMLInputElement>event.target;

      if (el.value != "") return;

      var parent = (<HTMLElement> el).parentElement;
      var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

      text.style.top = "50%";
      text.style.fontSize = "16px";
      text.style.color = "black";
    }, 220);
  }

  showDatepicker(): void {
    const datePicker = <HTMLElement> this.datepicker.nativeElement;
    clearTimeout(this.datepickerTimeout);
    datePicker.classList.remove("remove-date-picker");
    this.datepickerTimeout = setTimeout(() => {
      datePicker.classList.remove("hide-date-picker");
    }, 100);
    
  }

  @HostListener('window:click', ['$event'])
  removeDatePicker(event) {
    var target = event.target;
    if (target == this.searchDate.nativeElement) return;
    
    if (target.classList.contains('dl-abdtp-date-button')) {
      if (!target.classList.contains('dl-abdtp-disabled')) {
        var el = this.searchDate.nativeElement;
        var parent = (<HTMLElement> el).parentElement;
        var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

        text.style.top = "-6px";
        text.style.fontSize = "12px";
        text.style.color = "darkcyan";
        this.inputChanged();
      }
      return;
    }

    if (target.closest('.date-picker-holder') != null) return;

    const datePicker = <HTMLElement> this.datepicker.nativeElement;
    clearTimeout(this.datepickerTimeout);
    datePicker.classList.add('hide-date-picker');
    this.datepickerTimeout = setTimeout(() => {
      datePicker.classList.add('remove-date-picker');
    }, 500);
  }


}
