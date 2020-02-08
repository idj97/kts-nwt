import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { LocationService } from '../../services/location.service';
import { LayoutService } from '../../services/layout.service';
import { ToasterService } from '../../services/toaster.service';
import { Location } from '../../models/location.model';

@Component({
  selector: 'app-update-location',
  templateUrl: './update-location.component.html',
  styleUrls: ['./update-location.component.css']
})
export class UpdateLocationComponent implements OnInit {
  createLocationForm: FormGroup;
  layoutMappings: Map<string, number>;
  layouts: Array<string>;
  location: Location = new Location();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private locationService: LocationService,
    private layoutService: LayoutService,
    private toasterService: ToasterService
  ) {
    this.createLocationForm = this.fb.group({
      name: ['', [Validators.required]],
      address: ['', [Validators.required]],
      layoutName: ['', [Validators.required]],
    });

    this.getNameMappings();
    this.getLocation();
  }

  ngOnInit() {
  }

  getLocation() {
    // tslint:disable-next-line: radix
    const locationId = parseInt(this.activatedRoute.snapshot.paramMap.get('id'));

    this.locationService.getById(locationId).subscribe(
      data => {
        this.createLocationForm.controls.name.setValue(data.name);
        this.createLocationForm.controls.address.setValue(data.address);
        this.createLocationForm.controls.layoutName.setValue(data.layoutName);
        this.location = data;
      },
      error => {
        this.toasterService.showMessage('', error.error.message);
      }
    );

  }

  getNameMappings() {
    this.layoutService.getNameIdMappings().subscribe(
      data => {
        this.layoutMappings = data;
        this.layouts = Object.keys(this.layoutMappings);
      },
      error => {
        this.toasterService.showMessage('', error.error.message);
      }
    );
  }

  onSubmit(locationData: any) {
    this.location.name = locationData.name;
    this.location.address = locationData.address;
    this.location.layoutId = this.layoutMappings[locationData.layoutName];

    this.locationService.updateLocation(this.location).subscribe(
      _ => {
        this.toasterService.showMessage('Success', 'Location updated successfully!');
        this.router.navigate(['/manage-locations']);
      },
      error => this.toasterService.showMessage('', error.error.message)
    );

  }

}
