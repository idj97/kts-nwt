import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'div [app-theater-layout]',
  templateUrl: './theater-layout.component.html',
  styleUrls: ['./theater-layout.component.css']
})
export class TheaterLayoutComponent implements OnInit {

  @Input() public isEditing: boolean = false;

  constructor() { }

  ngOnInit() {
  }

}
