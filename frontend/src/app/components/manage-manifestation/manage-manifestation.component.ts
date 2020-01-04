import { Component, OnInit } from '@angular/core';
import { Manifestation } from '../../models/manifestation.model';
import { ManifestationService } from '../../services/manifestation.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, FormArray } from '@angular/forms';

@Component({
  selector: 'app-manage-manifestation',
  templateUrl: './manage-manifestation.component.html',
  styleUrls: ['./manage-manifestation.component.css']
})

export class ManageManifestationComponent implements OnInit {

  title: string;
  manifestation: Manifestation;
  manifestationForm: FormGroup;


  constructor(private manifService: ManifestationService, private route: ActivatedRoute) { 
    this.manifestationForm = this.createManifestationFormGroup();
  }

  ngOnInit() {

    this.route.params.subscribe(
      params => {
        if(params['id'] !== undefined) {
          this.title = "Edit manifestation";
        } else {
          this.title = "Create manifestation"
        }
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
  }

  displayReservationData() {
    // reset reservation data
    this.manifestationForm.controls['maxReservations'].setValue(null);
    this.manifestationForm.controls['reservableUntil'].setValue(null);

    $(".hidden-element").toggle();
  }

}
