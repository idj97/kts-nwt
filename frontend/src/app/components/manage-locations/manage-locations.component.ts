import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UtilityService } from '../../services/utility.service';
import { LocationService } from '../../services/location.service';
import { ToasterService } from '../../services/toaster.service';
import { LayoutService } from '../../services/layout.service';

@Component({
  selector: 'app-manage-locations',
  templateUrl: './manage-locations.component.html',
  styleUrls: ['./manage-locations.component.css']
})
export class ManageLocationsComponent implements OnInit {
  locations: Location[];
  searchName: string;
  searchAddress: string;
  activePage: number;
  numberOfPages: number;

  constructor(
    private router: Router,
    private utilityService: UtilityService,
    private locationService: LocationService,
    private layoutService: LayoutService,
    private toastrService: ToasterService
  ) {
    this.utilityService.setNavbar();
    this.getLocations();
  }

  ngOnInit() {
    this.utilityService.resetNavbar();
    document.getElementById('navbar').style.boxShadow = 'none';
    document.getElementById('navbar').style.borderBottom = '2px solid black';
  }

  getLocations() {
    this.locationService.searchLocations().subscribe(
      data => {
        this.locations = data.page;
        this.numberOfPages = data.totalNumberOfPages;
      },
      error => {
        this.toastrService.showMessage('', error.error.message);
      }
    );
  }

  searchLocations(page: number) {
    this.activePage = page;
    this.searchName = ((document.getElementById('name')) as HTMLInputElement).value.trim();
    this.searchAddress = ((document.getElementById('address')) as HTMLInputElement).value.trim();

    this.locationService.searchLocations(this.searchName, this.searchAddress, this.activePage).subscribe(
      data => {
        this.locations = data.page;
        this.numberOfPages = data.totalNumberOfPages;
      },
      error => {
        this.toastrService.showMessage('', error.error.message);
      }
    );
  }

  addLocation() {
    this.router.navigateByUrl('/manage-locations/create');
  }

  updateLocation(locationId: number) {
    this.router.navigateByUrl(`/manage-locations/update/${locationId}`);
  }

  deleteLocation(locationId: number, index: number) {
    this.locationService.deleteLocation(locationId).subscribe(
      _ => {
       this.locations.splice(index, 1);
      },
      error => {
        this.toastrService.showMessage('', error.error.message);
      }
    );
  }

  focusInput(event: FocusEvent) {
    const el = event.target;
    const parent = ( el as HTMLElement).parentElement;
    const text =  (parent.getElementsByClassName('input-text-value')[0]) as HTMLElement;

    text.style.top = '-6px';
    text.style.fontSize = '12px';
    text.style.color = 'darkcyan';
  }

  blurInput(event: FocusEvent) {
    const el =  event.target as HTMLInputElement;

    if (el.value !== '') { return; }

    const parent = ( el as HTMLElement).parentElement;
    const text =  ( parent.getElementsByClassName('input-text-value')[0]) as HTMLElement;

    text.style.top = '50%';
    text.style.fontSize = '16px';
    text.style.color = 'black';
  }

}
