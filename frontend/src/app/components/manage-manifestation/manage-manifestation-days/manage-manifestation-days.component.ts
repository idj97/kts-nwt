import { Component, OnInit, Input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs';
import { DateButton } from 'angular-bootstrap-datetimepicker';
import { ToasterService } from 'src/app/services/toaster.service';

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

  constructor(private datePipe: DatePipe, private toastService: ToasterService) { }

  ngOnInit() {

    if (this.editing) {
      this.displayPopUp();
    }

  }

  addManifestationDate() {

    this.displayPopUp();

    if (this.selectedDate == undefined) {
      return;
    }

    if (!this.manifestationDayAdded(this.selectedDate)) {
      this.manifestationDays.push(this.selectedDate);
    } else {
      this.toastService.showMessage('Failed to add day', 'The day you selected has already been added');
    }

  }

  removeManifestationDate(dateIndex: number) {
    this.manifestationDays.splice(dateIndex, 1);
  }

  manifestationDayAdded(dayToCheck: Date): boolean {
    for (const manifDay of this.manifestationDays) {
      if (this.formatDate(manifDay) === this.formatDate(dayToCheck)) {
        return true;
      }
    }
    return false;
  }

  formatDate(dateToFormat: Date): string {
    return this.datePipe.transform(dateToFormat, 'yyyy-MM-dd');
  }

  displayPopUp(): void {
    this.popUpWidth = '20%';
  }

  closePopUp(): void {
    this.popUpWidth = '0';
  }

  futureDatesOnly(dateButton: DateButton, viewName: string) {
    return dateButton.value > (new Date()).getTime();
  }


}
