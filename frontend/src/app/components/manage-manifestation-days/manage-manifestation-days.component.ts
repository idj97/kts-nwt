import { Component, OnInit, Input } from '@angular/core';
import { FormArray, FormControl } from '@angular/forms';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-manage-manifestation-days',
  templateUrl: './manage-manifestation-days.component.html',
  styleUrls: ['./manage-manifestation-days.component.css']
})
export class ManageManifestationDaysComponent implements OnInit {

  selectedDate: Date;
  popUpWidth: string;
  @Input() manifestationDays: FormArray;

  constructor(private datePipe: DatePipe) { }

  ngOnInit() {
  }

  addManifestationDate() {
    this.popUpWidth = '20%';

    if(this.selectedDate != undefined && !this.manifestationDayAdded(this.selectedDate)) {
      this.manifestationDays.push(new FormControl(this.selectedDate));
    }
  }

  formatDate(dateToFormat: Date): string {
    return this.datePipe.transform(dateToFormat, 'yyyy-MM-dd');
  }

  removeManifestationDate(dateIndex: number) {
    this.manifestationDays.removeAt(dateIndex);
  }

  manifestationDayAdded(dayToCheck: Date): boolean {
    for(let manifDay of this.manifestationDays.controls) {
      if(this.formatDate(manifDay.value) == this.formatDate(dayToCheck)) {
        return true;
      }
    }
    return false;
  }

  closePopUp(): void {
    this.popUpWidth = '0';
  }


}
