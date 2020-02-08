import { Component, OnInit } from '@angular/core';
import { LocationService } from 'src/app/services/location.service';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { Location } from '../../models/location.model';
import { LocationReport } from 'src/app/models/location-report.model';
import { FormGroup, FormControl } from '@angular/forms';
import { UtilityService } from 'src/app/services/utility.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  locations: Array<Location>;
  manifestations: Array<Manifestation>;

  reportForm: FormGroup;

  // chart variables
  barChartData: Array<any>;
  barChartLabels: Array<string>;
  barChartOptions: any;
  barChartLegend: boolean;
  barChartType: string;

  constructor(
    private utilityService: UtilityService,
    private locationSvc: LocationService,
    private manifestationSvc: ManifestationService
  ) {
    this.reportForm = this.createReportFormGroup(); 
    this.manifestations = [];
    this.locations = [];
  }

  ngOnInit() {

    this.initChart();
    this.getAllManifestations();
    this.getAllLocations();
    this.utilityService.resetNavbar();

  }

  initChart(): void {
    this.barChartOptions = {
      scaleShowVerticalLines: false,
      responsive: true
    };

    this.barChartLegend = true;
    this.barChartType = 'bar';

    this.barChartLabels = [];
    this.barChartData = [];
  }

  createReportFormGroup(): FormGroup {

    return new FormGroup({
      reportType: new FormControl(''),
      locationId: new FormControl(0),
      manifestationId: new FormControl(0),
      startDate: new FormControl(new Date()),
      endDate: new FormControl(null)
    });

  }

  get getReportType() {
    return this.reportForm.controls['reportType'].value;
  }

  getAllLocations() {
    this.locationSvc.getAllLocations().subscribe(
      data => {
        this.locations = data;
      }
    );
  }

  getAllManifestations() {
    this.manifestationSvc.getAllManifestations().subscribe(
      data => {
        this.manifestations = data;
      }
    );
  }

  getTicketReport() {
    console.log(this.reportForm);
  }

  getProfitReport() {
    console.log(this.reportForm)
  }

}
