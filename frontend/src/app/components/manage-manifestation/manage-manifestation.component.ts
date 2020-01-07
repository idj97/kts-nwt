import { Component, OnInit } from '@angular/core';
import { Manifestation } from '../../models/manifestation.model';
import { ManifestationService } from '../../services/manifestation.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { LocationService } from 'src/app/services/location.service';

@Component({
  selector: 'app-manage-manifestation',
  templateUrl: './manage-manifestation.component.html',
  styleUrls: ['./manage-manifestation.component.css']
})

export class ManageManifestationComponent implements OnInit {

  title: string;
  manifestation: Manifestation;
  manifestationTypes: Array<string>;
  locations: Array<Location>;

  manifestationForm: FormGroup;


  constructor (
    private manifService: ManifestationService,
    private locationService: LocationService,
    private route: ActivatedRoute) {
     
    this.manifestationForm = this.createManifestationFormGroup();
    this.manifestationTypes = ['Culture', 'Sport', 'Entertainment'];
  }

  ngOnInit() {

    this.route.params.subscribe(
      params => {
        if(params['id'] !== undefined) {
          // TODO: fetch manifestation from server
          this.title = "Edit manifestation";
        } else {
          this.title = "Create manifestation"
        }

        this.getLocations();
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

  createManifestationFormGroup(): FormGroup {
    return new FormGroup({
      name: new FormControl(null, Validators.required),
      description: new FormControl(null, Validators.required),
      type: new FormControl(null, Validators.required),

      manifestationDates: new FormArray([], Validators.required),
      images: new FormArray([]),
      selectedSections: new FormArray([]),
      
      reservationsAllowed: new FormControl(false),
      maxReservations: new FormControl(), 
      reservableUntil: new FormControl(),
      locationId: new FormControl(null, Validators.required)
    });
  }

  
  submitManifestation() {
    if(this.manifestationForm.valid) {
      this.manifestation = this.manifestationForm.value;
      this.manifService.createManifestation(this.manifestation).subscribe(
        data => {
          this.manifestation = data;
        },
        error => {
          console.log(error.error);
        }
      )
    }
  }

  clearReservationData() {
    // reset reservation data
    this.manifestationForm.controls['maxReservations'].setValue(null);
    this.manifestationForm.controls['reservableUntil'].setValue(null);
  }


}