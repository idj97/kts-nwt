import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { UtilityService } from 'src/app/services/utility.service';
import { LayoutService } from 'src/app/services/layout.service';
import { Location } from 'src/app/models/location.model';
import { Layout } from 'src/app/models/layout';
import { ToasterService } from 'src/app/services/toaster.service';
import { Subject, Subscription } from 'rxjs';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { SectionService } from 'src/app/services/section.service';

@Component({
  selector: 'app-manage-manifestation-sections',
  templateUrl: './manage-manifestation-sections.component.html',
  styleUrls: ['./manage-manifestation-sections.component.css']
})
export class ManageManifestationSectionsComponent implements OnInit, OnDestroy {

  selectedLocation: Location;
  layout: Layout;

  displaySections: Array<any>;
  notifyUpdateEdit: Subject<any>;

  sectionSubscription: Subscription;
  @Input() selectedSections: Array<any>;

  constructor (
    private utilSvc: UtilityService,
    private layoutSvc: LayoutService,
    private sectionSvc: SectionService,
    private toastSvc: ToasterService
    ) { }

  ngOnInit() {
    this.displaySections = [];
    this.notifyUpdateEdit = new Subject<any>();

    this.utilSvc.resetNavbar();

    this.sectionSubscription = this.sectionSvc.getPreviousSections().subscribe(
      data => {
        this.insertPreviousSections(data);
      }
    );
  }

  @Input()
  set location(location: Location) {
    this.selectedLocation = location;

    if(this.selectedLocation != null) {
      this.getLayoutById(this.selectedLocation.id);
    }
    
  }

  insertPreviousSections(sections: Array<any>) {
    sections.forEach(
      section => {

        // changing display of sections
        setTimeout(() => {
          this.notifyUpdateEdit.next({
            sectionId: section.selectedSectionId,
            totalSelected: section.size-1,
            ticketPrice: section.price
          });
        }, 600);
      }
    );
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
        if(section.isDisabled) {
          this.removeSection(i);
        } else {
          this.updateSection(this.selectedSections[i], section);
        }
        return;
      }
    }

    // insert new section
    if(!section.isDisabled) {
      this.selectedSections.push(this.createNewSection(section));
    }
    
  }

  createNewSection(section:any): ManifestationSection {

    let manifSection = new ManifestationSection();

    manifSection.selectedSectionId = section.sectionId;
    manifSection.size = section.totalSelected; 
    manifSection.price = section.ticketPrice; 
    
    return manifSection;
  }

  updateSection(manifSection: ManifestationSection, section: any) {
    manifSection.selectedSectionId = section.sectionId;
    manifSection.size = section.totalSelected; 
    manifSection.price = section.ticketPrice; 
  }

  removeSection(sectionIndex: number) {
    this.selectedSections.splice(sectionIndex, 1);
  }

  ngOnDestroy() {
    this.sectionSubscription.unsubscribe();
  }

}
