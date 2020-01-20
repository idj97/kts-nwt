import { Component, OnInit, OnDestroy } from '@angular/core';
import { Manifestation } from '../../models/manifestation.model';
import { ManifestationService } from '../../services/manifestation.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators, FormArray} from '@angular/forms';
import { LocationService } from 'src/app/services/location.service';
import { maxReservationsValidator, reservableUntilValidator } from 'src/app/validators/manifestation.validator';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-manage-manifestation',
  templateUrl: './manage-manifestation.component.html',
  styleUrls: ['./manage-manifestation.component.css']
})

export class ManageManifestationComponent implements OnInit {

  editing: boolean;
  submitClicked: boolean;
  
  manifestationTypes: Array<string>;
  manifestationImages: Array<any>;
  locations: Array<Location>;

  manifestation: Manifestation;
  manifestationForm: FormGroup;

  constructor (
    private manifService: ManifestationService,
    private locationService: LocationService,
    private toastService: ToasterService,
    private route: ActivatedRoute
    ) {
    
    this.manifestationTypes = ['CULTURE', 'SPORT', 'ENTERTAINMENT'];
    this.manifestationImages = [];

    this.submitClicked = false;
    this.manifestationForm = this.createManifestationFormGroup(new Manifestation());
  }

  ngOnInit() {

    this.route.params.subscribe(
      params => {
        if(params['id'] !== undefined) {
          this.getManifestationById(params['id']);
          this.editing = true;
        } else {
          this.editing = false;
        }

        this.getLocations();
      }
    )
  }

  getManifestationById(id) {
    this.manifService.getManifestationById(id).subscribe(
      data => {
        this.manifestationForm = this.createManifestationFormGroup(data);
        this.setManifestationDates(data.manifestationDates);
      },
      err => {
        console.log(err.error);
      }
    )
  }

  getLocations() {
    this.locationService.getAllLocations().subscribe(
      data => {
        this.locations = data;
      },
      error => {
        this.toastService.showErrorMessage(error);
      }
    )
  }

  createManifestationFormGroup(manifestation: Manifestation): FormGroup {
    
    return new FormGroup({
      manifestationId: new FormControl(manifestation.manifestationId),
      name: new FormControl(manifestation.name, [Validators.required]),
      description: new FormControl(manifestation.description, [Validators.required]),
      type: new FormControl(manifestation.type, Validators.required),

      manifestationDates: new FormArray([]),
      images: new FormArray([]),
      selectedSections: new FormArray([]),
      
      reservationsAllowed: new FormControl(manifestation.reservationsAllowed),
      maxReservations: new FormControl(manifestation.maxReservations), 
      reservableUntil: new FormControl(manifestation.reservableUntil != null ? this.getReservableUntil(manifestation.reservableUntil): null),
      locationId: new FormControl(manifestation.locationId, Validators.required)
    }, { validators: [reservableUntilValidator, maxReservationsValidator] });
  }

  getReservableUntil(reservableUntil): string {
    return reservableUntil.split('T')[0];
  }

  get getManifestationDates() {
    return this.manifestationForm.controls['manifestationDates'] as FormArray;
  }

  setManifestationDates(dates: Array<Date>) {
    dates.forEach(date => {
      this.getManifestationDates.push(new FormControl(date));
    });
  }

  get areReservationsAllowed() {
    return this.manifestationForm.controls['reservationsAllowed'].value;
  }
  
  submitManifestation() {
    
    this.submitClicked = true;
    if(!this.manifestationForm.valid || this.getManifestationDates.value.length == 0) {
      return;
    }

    this.displaySpinner();

    this.manifestation = this.manifestationForm.value;
    if(this.editing) {
      this.updateManifestation();
    } else {
      this.createManifestation();
    }

  }

  createManifestation() {

    this.manifService.createManifestation(this.manifestation).subscribe(
      data => {
        this.manifestation = data;
        this.toastService.showMessage('Success', 'Manifestation successfully created');
        this.uploadImages(data.manifestationId);
      },
      error => {
        this.toastService.showErrorMessage(error);
      }
    ).add(
      () => {
        this.hideSpinner();
      }
    );

  }

  updateManifestation() {

    this.manifService.updateManifestation(this.manifestation).subscribe(
      data => {
        this.manifestation = data;
        this.toastService.showMessage('Success', 'Manifestation successfully updated');
      },
      err => {
        this.toastService.showErrorMessage(err);
      }
    ).add(
      () => {
        this.hideSpinner();
      }
    )

  }

  uploadImages(manifestationId: number): void {

    // prep files for post
    const uploadData = new FormData();
    this.manifestationImages.forEach(
      image => uploadData.append('manifestation-images', image)
    );

    this.manifService.uploadImages(uploadData, manifestationId).subscribe(
      data => {
        console.log(data);
      },
      err => {
        this.toastService.showErrorMessage(err);
      }
    )

  }

  clearReservationData() {
    this.manifestationForm.controls['maxReservations'].setValue(null);
    this.manifestationForm.controls['reservableUntil'].setValue(null);
  }


  displaySpinner(): void {
    document.getElementById('manifestation-spinner').style.visibility = 'visible';
  }

  hideSpinner(): void {
    document.getElementById('manifestation-spinner').style.visibility = 'hidden';
  }


}
