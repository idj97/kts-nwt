import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../../services/authentication.service';

@Component({
  selector: 'div [class="col-lg-6"] [app-manifestation-item]',
  templateUrl: './manifestation-item.component.html',
  styleUrls: ['./manifestation-item.component.css']
})
export class ManifestationItemComponent implements OnInit {


  public name: String;
  public description: String;
  public location: String;
  public price: Number;
  public availableTickets: Number;
  public date: String;
  public id: Number;

  private isAdmin;

  constructor(private authService: AuthenticationService) { }

  ngOnInit() {
    this.isAdmin = this.authService.isBasicAdmin();
  }



}
