import { Component, OnInit, Input } from '@angular/core';
import { UtilityService } from 'src/app/services/utility.service';
import { LayoutService } from 'src/app/services/layout.service';
import { Location } from 'src/app/models/location.model';
import { Layout } from 'src/app/models/layout';
import { ToasterService } from 'src/app/services/toaster.service';
import { Subject } from 'rxjs';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';

@Component({
  selector: 'app-manage-manifestation-sections',
  templateUrl: './manage-manifestation-sections.component.html',
  styleUrls: ['./manage-manifestation-sections.component.css']
})
export class ManageManifestationSectionsComponent implements OnInit {

  selectedLocation: Location;
  layout: Layout;

  displaySections: Array<any>;
  notifyUpdateEdit: Subject<any>;

  @Input() selectedSections: Array<any>;

  constructor (
    private utilSvc: UtilityService,
    private layoutSvc: LayoutService,
    private toastSvc: ToasterService
    ) { }

  ngOnInit() {
    this.displaySections = [];
    this.notifyUpdateEdit = new Subject<any>();

    this.utilSvc.resetNavbar();
  }

  @Input()
  set location(location: Location) {
    this.selectedLocation = location;

    if(this.selectedLocation != null) {
      this.getLayoutById(this.selectedLocation.id);
    }
    
  }

  getLayoutById(id: number) {
    this.layoutSvc.getById(id).subscribe(
      data => {
        this.layout = data;
        this.displaySections = this.utilSvc.getDisplaySectionsForLayout(this.layout, null);
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    );
  }

  retrieveSelectedSeatsEdit(event: any) {
    this.insertSelectedSection(event);
  }

  retrieveSelectedNoSeatsEdit(event: any) {
    this.insertSelectedSection(event);
  }

  
  insertSelectedSection(section: any): void {
    
    // if the selection has already been added, overwrite it
    for(let i = 0; i < this.selectedSections.length; i++) {
      if(section.sectionId == this.selectedSections[i].selectedSectionId) {
        this.updateSection(this.selectedSections[i], section);
        return;
      }
    }

    // insert new section
    this.selectedSections.push(this.createNewSection(section));
  }

  createNewSection(section:any): ManifestationSection {

    let manifSection = new ManifestationSection();

    manifSection.selectedSectionId = section.sectionId;
    manifSection.size = section.isDisabled ? 0: section.totalSelected; 
    manifSection.price = 0;//TODO: set price

    return manifSection;
  }

  updateSection(manifSection: ManifestationSection, section: any) {
    manifSection.selectedSectionId = section.sectionId;
    manifSection.size = section.isDisabled ? 0: section.totalSelected; 
    manifSection.price = 0;//TODO: set price
  }


}
