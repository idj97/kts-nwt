import { Injectable } from '@angular/core';
import { Section } from '../models/section';
import { ManifestationSection } from '../models/manifestation-section.model';
import { Layout } from '../models/layout';


@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor() { }

  public getDisplaySectionsForLayout(layout: Layout, manifestationSections: ManifestationSection[]) {
    var displaySections = [];
    if (layout.name == "STADIUM") {
      for (var i = 0; i < layout.sections.length; i++) {
        if (layout.sections[i].name == "PARTER") {
          displaySections[0] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "NORTH") {
          displaySections[1] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "EAST") {
          displaySections[2] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "SOUTH") {
          displaySections[3] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "WEST") {
          displaySections[4] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
      }
    }

    if (layout.name == "THEATER") {
      for (var i = 0; i < layout.sections.length; i++) {
        if (layout.sections[i].name == "CLASS_1") {
          displaySections[0] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "CLASS_2") {
          displaySections[1] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "CLASS_3") {
          displaySections[2] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "CLASS_4") {
          displaySections[3] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
      }
    }

    if (layout.name == "OPEN_SPACE") {
      for (var i = 0; i < layout.sections.length; i++) {
        if (layout.sections[i].name == "AREA_1") {
          displaySections[0] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "AREA_2") {
          displaySections[1] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "AREA_3") {
          displaySections[2] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
        else if (layout.sections[i].name == "AREA_4") {
          displaySections[3] = {
            section: layout.sections[i],
            manifestationSection: this.matchManifestationSectionBySection(manifestationSections, layout.sections[i])
          };
        }
      }
    }

    return displaySections;
  }

  public matchManifestationSectionBySection(manifestationSections: ManifestationSection[], section: Section) {
    if (manifestationSections == null) return null;
    for (var i = 0; i < manifestationSections.length; i++) {
      if (manifestationSections[i].selectedSectionId == section.id) {
        return manifestationSections[i];
      }
    }

    return null;
  }

  public setNavbar() {
    if (window.pageYOffset > 0) {
      this.resetNavbar();
    }
    else {
      var navbar = document.getElementById("navbar");
      var navLinks = document.getElementsByClassName("nav-link");
      var navBrand = document.getElementsByClassName("navbar-brand")[0];
      var navBarToggler = document.getElementsByClassName("navbar-toggler-icon")[0];
      navbar.style.borderBottom = "none";
      navbar.classList.remove("bg-white");
      navbar.style.boxShadow = "0px 0px 15px 0px rgba(0,0,0,0.0)";
      navBrand.classList.remove("nav-black-color");
      navBrand.classList.add("nav-white-color");
      navBarToggler.classList.remove("nav-bar-toggler-icon-black");
      for (var i = 0; i < navLinks.length; i++) {
        navLinks[i].classList.remove("nav-black-color");
        navLinks[i].classList.add("nav-white-color");
      }
    }
  }

  public resetNavbar() {
    
    var navbar = document.getElementById("navbar");
    var navLinks = document.getElementsByClassName("nav-link");
    var navBrand = document.getElementsByClassName("navbar-brand")[0];
    var navBarToggler = document.getElementsByClassName("navbar-toggler-icon")[0];
    navbar.style.borderBottom = "none";
    navbar.classList.add("bg-white");
    navbar.style.boxShadow = "0px 0px 15px 0px rgba(0,0,0,0.5)";
    navBrand.classList.remove("nav-white-color");
    navBrand.classList.add("nav-black-color");
    navBarToggler.classList.add("nav-bar-toggler-icon-black");
    for (var i = 0; i < navLinks.length; i++) {
      navLinks[i].classList.remove("nav-white-color");
      navLinks[i].classList.add("nav-black-color");
    }
  }

}
