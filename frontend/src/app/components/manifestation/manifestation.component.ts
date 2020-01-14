import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';
import { Title } from '@angular/platform-browser';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { Manifestation } from 'src/app/models/manifestation.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-manifestation',
  templateUrl: './manifestation.component.html',
  styleUrls: ['./manifestation.component.css'],
  providers: [DatePipe]
})
export class ManifestationComponent implements OnInit {

  private id: Number;

  private name: String;
  private description: String;
  private date: String;

  constructor(private route: ActivatedRoute,
    private utilityService: UtilityService,
    private titleService: Title,
    private manifestationService: ManifestationService,
    private datePipe: DatePipe) {

      this.route.params.subscribe(
        params => {
          this.id = params['id'];
        }
      )


      this.utilityService.setNavbar();
  }

  ngOnInit() {
    this.setUpManifestation();
  }


  private setUpManifestation() {
    this.manifestationService.getManifestationById(this.id).subscribe(
      data => {
        var man = <Manifestation> data;

        this.name = man.name;
        this.description = man.description;
        this.date = this.datePipe.transform(man.reservableUntil, "dd.MM.yyyy HH:mm");

        this.titleService.setTitle('m-booking | ' + this.name);
      },

      error => {
        console.log(error);
      }
    )
  }

}
