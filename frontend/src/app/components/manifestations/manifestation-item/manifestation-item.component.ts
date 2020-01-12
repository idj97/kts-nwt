import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'div [class="col-lg-6"] [app-manifestation-item]',
  templateUrl: './manifestation-item.component.html',
  styleUrls: ['./manifestation-item.component.css']
})
export class ManifestationItemComponent implements OnInit {


  public name: String = "test";
  public description: String = "test";
  public location: String = "test";
  public price: Number = 55;
  public availableTickets: Number = 25;
  public date: String = "test";

  constructor() { }

  ngOnInit() {
  }

}
