import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'div [app-stadium-layout]',
  templateUrl: './stadium-layout.component.html',
  styleUrls: ['./stadium-layout.component.css']
})
export class StadiumLayoutComponent implements OnInit {

  public isEditing: boolean = true;

  constructor() { }

  ngOnInit() {
  }

  @HostListener('window:click')
  ccc() {
    //this.isEditing = !this.isEditing;
  }

}
