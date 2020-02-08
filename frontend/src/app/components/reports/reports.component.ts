import { Component, OnInit } from '@angular/core';
import { LocationService } from 'src/app/services/location.service';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { Location } from '../../models/location.model';
import { FormGroup, FormControl } from '@angular/forms';
import { UtilityService } from 'src/app/services/utility.service';
import { Report } from 'src/app/models/report.model';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  locations: Array<Location>;
  manifestations: Array<Manifestation>;

  reportForm: FormGroup;
  report: Report;

  // chart variables
  barChartData: Array<any>;
  barChartLabels: Array<string>;
  barChartOptions: any;
  barChartLegend: boolean;
  barChartType: string;

  constructor(
    private toaster: ToasterService,
    private utilityService: UtilityService,
    private locationSvc: LocationService,
    private manifestationSvc: ManifestationService
  ) {
    this.reportForm = this.createReportFormGroup(); 
    this.manifestations = [];
    this.locations = [];
    this.report = new Report();
    this.initChart();
  }

  ngOnInit() {

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

  get getManifestationId() {
    return this.reportForm.controls['manifestationId'].value;
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


  /** displayType - profit or ticket */
  getReport(displayType: string): void {

    if (this.getReportType == 'location') {
      this.getLocationReport(displayType);
    } else if (this.getReportType == 'manifestation') {
      this.getManifestationReport(displayType);
    } else {
      this.toaster.showMessage('Failed', 'Please select a report type');
    }

  }

  getManifestationReport(displayType: string): void {
    this.manifestationSvc.getReports(this.getManifestationId).subscribe(
      data => {
        this.report = data;
        this.updateChart(displayType);
      },
      err => {
        this.toaster.showErrorMessage(err);
      }
    );
  }

  getLocationReport(displayType: string): void {
    let locationReport = this.reportForm.value;
    this.locationSvc.getReports(locationReport).subscribe(
      data => {
        this.report = data;
        this.updateChart(displayType);
      },
      err => {
        this.toaster.showErrorMessage(err);
      }
    );
  }

  updateChart(displayType: string) {

    if (displayType == 'ticket') {
      this.barChartLabels = this.report.labels;
      this.barChartData = [{data: this.report.ticketData, label: displayType, backgroundColor: '#4287f5', scaleFontColor: "#FFFFFF"}];
    } else {
      this.barChartLabels = this.report.labels;
      this.barChartData = [{data: this.report.incomeData, label: displayType,  backgroundColor: '#4287f5', scaleFontColor: "#000000"}];
    }

    if(this.barChartData.length === 0 || this.barChartLabels.length === 0) {
      this.toaster.showMessage('Information', 'No tickets have been sold');
    }

  }

}
