import { Component, OnInit } from '@angular/core';
import { Manifestation } from '../../models/manifestation.model';
import { ManifestationService } from '../../services/manifestation.service';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormGroup, FormControl, Validators, FormArray} from '@angular/forms';
import { LocationService } from 'src/app/services/location.service';
import { maxReservationsValidator, reservableUntilValidator } from 'src/app/validators/manifestation.validator';
import { ToasterService } from 'src/app/services/toaster.service';
import { ManifestationImage } from 'src/app/models/manifestation-image-model';
import { Location } from 'src/app/models/location.model';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { SectionService } from 'src/app/services/section.service';

@Component({
  selector: 'app-manage-manifestation',
  templateUrl: './manage-manifestation.component.html',
  styleUrls: ['./manage-manifestation.component.css']
})

export class ManageManifestationComponent implements OnInit {

  editing: boolean;
  submitClicked: boolean;
  
  manifestationTypes: Array<string>;
  imagesToUpload: Array<any>;
  locations: Array<Location>;
  selectedLocation: Location;

  manifestation: Manifestation;
  manifestationForm: FormGroup;

  constructor (
    private manifService: ManifestationService,
    private locationService: LocationService,
    private sectionService: SectionService,
    private toastService: ToasterService,
    private route: ActivatedRoute,
    private router: Router
    ) {
    
    this.manifestationTypes = ['CULTURE', 'SPORT', 'ENTERTAINMENT'];
    this.imagesToUpload = [];
    this.locations = [];

    this.submitClicked = false;
    this.manifestationForm = this.createManifestationFormGroup(new Manifestation());
  }

  ngOnInit() {
  
    this.getLocations();
    
    this.route.params.subscribe(
      params => {
        if(params['sectionId'] !== undefined) {
          this.getManifestationById(params['sectionId']);
          this.editing = true;
        } else {
          this.editing = false;
        }
      }
    );
  }

  getManifestationById(id) {
    this.manifService.getManifestationById(id).subscribe(
      data => {
        this.manifestationForm = this.createManifestationFormGroup(data);
        this.setManifestationDates(data.manifestationDates);
        this.setManifestationImages(data.images);
        this.updateSelectedLocation(data.locationId);
        this.sectionService.addPreviousSections(data.selectedSections);
        this.setSelectedSections(data.selectedSections);
      },
      err => {
        this.toastService.showErrorMessage(err);
        this.router.navigate(['/manage-manifestation']);
      }
    );
  }

  getLocations() {
    this.locationService.getAllLocations().subscribe(
      data => {
        this.locations = data;
      },
      error => {
        this.toastService.showErrorMessage(error);
      }
    );
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

  get getManifestationImages() {
    return this.manifestationForm.controls['images'] as FormArray;
  }

  setManifestationImages(images: Array<ManifestationImage>) {
    images.forEach(image => {
      this.getManifestationImages.push(new FormControl(image));
    });
  }

  get areReservationsAllowed() {
    return this.manifestationForm.controls['reservationsAllowed'].value;
  }

  get getSelectedSections() {
    return this.manifestationForm.controls['selectedSections'];
  }

  setSelectedSections(sections: Array<ManifestationSection>) {
    sections.forEach(
      section => {
        this.getSelectedSections.value.push(section);
      }
    )
  }

  updateSelectedLocation(event: any) {

    // if the function was called from the template it receives an event
    // if it was called from inside the file it directly received the sectionId
    let locationId = event.target == null ? event : event.target.value;
    
    for(let i = 0; i < this.locations.length; i++) {
      if(this.locations[i].id == locationId) {
        this.selectedLocation = this.locations[i];

        // clear the selected sections for previous location
        (this.getSelectedSections as FormArray).setValue([]);
        break;
      }
    }
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
        this.manifestationForm.reset(); // clear form inputs
        this.submitClicked = false; // to prevent error messages
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
        this.uploadImages(data.manifestationId);
      },
      err => {
        this.toastService.showErrorMessage(err);
      }
    ).add(
      () => {
        this.hideSpinner();
      }
    );

  }

  uploadImages(manifestationId: number): void {

    if(!this.imagesToUpload || this.imagesToUpload.length == 0) {
      return;
    }

    // prep files for post
    const uploadData = new FormData();
    this.imagesToUpload.forEach(
      image => uploadData.append('manifestation-images', image)
    );

    this.manifService.uploadImages(uploadData, manifestationId).subscribe(
      data => {
        console.log(data);
      },
      err => {
        this.toastService.showErrorMessage(err);
      }
    );

  }

  clearReservationData() {
    this.manifestationForm.controls['maxReservations'].setValue(null);
    this.manifestationForm.controls['reservableUntil'].setValue(null);
  }


  displaySpinner(): void {
    document.getElementById('submit-manifest-btn').style.visibility = 'hidden';
    document.getElementById('manifestation-spinner').style.visibility = 'visible';
  }

  hideSpinner(): void {
    document.getElementById('submit-manifest-btn').style.visibility = 'visible';
    document.getElementById('manifestation-spinner').style.visibility = 'hidden';
  }

  displaySections(): void {
    /*
    if(this.selectedLocation == null) {
      this.toastService.showMessage('Info', 'Please select a location in order to configure the sections');
      return;
    }*/
    document.getElementById('sections-pop-up').style.height = "100%";
  }

  hideSections(): void {
    document.getElementById('sections-pop-up').style.height = "0";
  }


}
