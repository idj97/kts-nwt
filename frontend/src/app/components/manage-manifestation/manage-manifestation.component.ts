import { Component, OnInit } from '@angular/core';
import { Manifestation } from '../../models/manifestation.model';
import { ManifestationService } from '../../services/manifestation.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, FormArray, FormBuilder } from '@angular/forms';
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

  manifestationForm: FormGroup;
  locations: FormArray;


  constructor (
    private manifService: ManifestationService,
    private locationService: LocationService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder) {
     
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
        this.locations = this.formBuilder.array(data);
        console.log(data);
      },
      error => {
        console.log(error.error);
      }
    )
  }

  createManifestationFormGroup(): FormGroup {
    return new FormGroup({
      name: new FormControl(),
      description: new FormControl(),
      type: new FormControl(),
      reservationsAllowed: new FormControl(false),
      maxReservations: new FormControl(), 
      reservableUntil: new FormControl(),
      locationId: new FormControl()
    });
  }

  submitManifestation() {
    console.log(this.manifestationForm.value);
    console.log(this.manifestationForm.valid);
  }

  displayReservationData() {
    // reset reservation data
    this.manifestationForm.controls['maxReservations'].setValue(null);
    this.manifestationForm.controls['reservableUntil'].setValue(null);

    $(".hidden-element").toggle();
  }

}
