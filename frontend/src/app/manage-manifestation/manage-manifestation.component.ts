import { Component, OnInit } from '@angular/core';
import { Manifestation } from '../models/manifestation.model';
import { ManifestationService } from '../services/manifestation.service';

@Component({
  selector: 'app-manage-manifestation',
  templateUrl: './manage-manifestation.component.html',
  styleUrls: ['./manage-manifestation.component.css']
})

export class ManageManifestationComponent implements OnInit {

  manifestation: Manifestation;

  constructor(private manifService: ManifestationService) { }

  ngOnInit() {
  }

}
