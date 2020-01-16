import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'div [app-open-space-layout]',
  templateUrl: './open-space-layout.component.html',
  styleUrls: ['./open-space-layout.component.css']
})
export class OpenSpaceLayoutComponent implements OnInit {

  @Input() public isEditing: boolean = false;

  constructor() { }

  ngOnInit() {
  }

}
