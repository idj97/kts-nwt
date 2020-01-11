import { Component, OnInit, Input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-manage-manifestation-days',
  templateUrl: './manage-manifestation-days.component.html',
  styleUrls: ['./manage-manifestation-days.component.css']
})
export class ManageManifestationDaysComponent implements OnInit {

  selectedDate: Date;
  popUpWidth: string;
  daysSubscription: Subscription;

  @Input() editing: boolean;
  @Input() submitClicked: boolean;
  @Input() manifestationDays: Array<any>;

  constructor(private datePipe: DatePipe) { }

  ngOnInit() {

    if(this.editing) {
      this.displayPopUp();
    }

  }

  addManifestationDate() {

    this.displayPopUp();

    if(this.selectedDate != undefined && !this.manifestationDayAdded(this.selectedDate)) {
      this.manifestationDays.push(this.selectedDate);
    }

  }

  removeManifestationDate(dateIndex: number) {
    this.manifestationDays.splice(dateIndex, 1);
  }

  manifestationDayAdded(dayToCheck: Date): boolean {
    for(let manifDay of this.manifestationDays) {
      if(this.formatDate(manifDay) == this.formatDate(dayToCheck)) {
        return true;
      }
    }
    return false;
  }

  formatDate(dateToFormat: Date): string {
    return this.datePipe.transform(dateToFormat, 'yyyy-MM-dd');
  }

  displayPopUp() {
    this.popUpWidth = '20%';
  }

  closePopUp(): void {
    this.popUpWidth = '0';
  }


}
