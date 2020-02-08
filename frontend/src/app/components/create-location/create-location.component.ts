import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToasterService } from '../../services/toaster.service';
import { LocationService } from '../../services/location.service';
import { LayoutService } from '../../services/layout.service';

import { Location } from '../../models/location.model';

@Component({
  selector: 'app-create-location',
  templateUrl: './create-location.component.html',
  styleUrls: ['./create-location.component.css']
})
export class CreateLocationComponent implements OnInit {
  createLocationForm: FormGroup;
  layoutMappings: Map<string, number>;
  layouts: Array<string>;

  constructor(
    private fb: FormBuilder,
    private router: Router,
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
  }

  ngOnInit() {
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
    const location: Location = new Location();
    location.name = locationData.name;
    location.address = locationData.address;
    location.layoutId = this.layoutMappings[locationData.layoutName];

    this.locationService.createLocation(location).subscribe(
      _ => {
        this.toasterService.showMessage('Success', 'Location created successfully!');
        this.router.navigate(['/manage-locations']);
      },
      error => this.toasterService.showMessage('', error.error.message)
    );

  }

}
