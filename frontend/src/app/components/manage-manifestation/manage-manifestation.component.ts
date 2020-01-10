import { Component, OnInit, OnDestroy } from '@angular/core';
import { Manifestation } from '../../models/manifestation.model';
import { ManifestationService } from '../../services/manifestation.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators, FormArray} from '@angular/forms';
import { LocationService } from 'src/app/services/location.service';

@Component({
  selector: 'app-manage-manifestation',
  templateUrl: './manage-manifestation.component.html',
  styleUrls: ['./manage-manifestation.component.css']
})

export class ManageManifestationComponent implements OnInit {

  editing: boolean;
  manifestation: Manifestation;

  manifestationTypes: Array<string>;
  locations: Array<Location>;

  manifestationForm: FormGroup;


  constructor (
    private manifService: ManifestationService,
    private locationService: LocationService,
    private route: ActivatedRoute
    ) {
    
    this.manifestationForm = this.createManifestationFormGroup(new Manifestation());
    this.manifestationTypes = ['CULTURE', 'SPORT', 'ENTERTAINMENT'];
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
        this.editing = false; // the manifestation wasn't found
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
        console.log(error.error);
      }
    )
  }

  createManifestationFormGroup(manifestation: Manifestation): FormGroup {
    
    return new FormGroup({
      name: new FormControl(manifestation.name, Validators.required),
      description: new FormControl(manifestation.description, Validators.required),
      type: new FormControl(manifestation.type, Validators.required),

      manifestationDates: new FormArray([]),
      images: new FormArray([]),
      selectedSections: new FormArray([]),
      
      reservationsAllowed: new FormControl(false),
      maxReservations: new FormControl(), 
      reservableUntil: new FormControl(),
      locationId: new FormControl(manifestation.locationId, Validators.required)
    });
  }

  get getManifestationDates() {
    return this.manifestationForm.controls['manifestationDates'] as FormArray;
  }

  setManifestationDates(dates: Array<Date>) {
    dates.forEach(date => {
      this.getManifestationDates.push(new FormControl(date));
    })
  }
  
  submitManifestation() {
    if(!this.manifestationForm.valid) {
      return;
    }

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
      },
      error => {
        console.log(error.error);
      }
    );

  }

  updateManifestation() {

  }

  clearReservationData() {
    this.manifestationForm.controls['maxReservations'].setValue(null);
    this.manifestationForm.controls['reservableUntil'].setValue(null);
  }


}
