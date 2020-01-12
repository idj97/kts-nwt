import { Component, OnInit, ViewChild, ViewContainerRef, AfterViewInit, ComponentFactoryResolver, Type, HostListener } from '@angular/core';
import { UtilityService } from 'src/app/services/utility.service';
import { Title } from '@angular/platform-browser';
import { ManifestationItemComponent } from './manifestation-item/manifestation-item.component';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-manifestations',
  templateUrl: './manifestations.component.html',
  styleUrls: ['./manifestations.component.css'],
  providers: [DatePipe]
})
export class ManifestationsComponent implements OnInit {

  @ViewChild('container', {static: true, read: ViewContainerRef}) container: ViewContainerRef;

  private manifestations: Manifestation[];

  constructor(private utilityService: UtilityService,
    private titleService: Title,
    private componentFactoryResolver: ComponentFactoryResolver,
    private manifestationService: ManifestationService,
    private datepipe: DatePipe) { 

      this.titleService.setTitle("m-booking | Manifestations");
      
    }

  ngOnInit() {
    this.utilityService.resetNavbar();
    document.getElementById("navbar").style.boxShadow = "none";
    document.getElementById("navbar").style.borderBottom = "2px solid black";

    this.setUpManifestations();
  }

  private setUpManifestations() {
    this.manifestationService.getAllManifestations().subscribe(
      data => {
        //Display manifestations
        this.manifestations = data;
        for (var i = 0; i < this.manifestations.length; i++) {
          var c = this.addComponent(ManifestationItemComponent);
          c.instance.name = this.manifestations[i].name;
          c.instance.description = this.manifestations[i].description;
          c.instance.date = this.datepipe.transform(this.manifestations[i].reservableUntil, "dd.MM.yyyy.");
          //TODO add more info
        }
      },
      error => {
        console.log(error.error);
      }
    )
  }

  addComponent(componentClass: Type<any>) {
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(componentClass);
    const component = this.container.createComponent(componentFactory);
    return component;
  }


  focusInput(event: FocusEvent) {
    var el = event.target;
    var parent = (<HTMLElement> el).parentElement;
    var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

    text.style.top = "-6px";
    text.style.fontSize = "12px";
    text.style.color = "darkcyan";
  }

  blurInput(event: FocusEvent) {

    var el = <HTMLInputElement>event.target;

    if (el.value != "") return;

    var parent = (<HTMLElement> el).parentElement;
    var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

    text.style.top = "50%";
    text.style.fontSize = "16px";
    text.style.color = "black";
  }

  

}
