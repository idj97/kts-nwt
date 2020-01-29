import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit, OnChanges  {
  @Input() numberOfPages: number;
  @Output() pageSelected: EventEmitter<number>;
  activePage: number;
  pages: number[];

  constructor() {
    this.pageSelected = new EventEmitter();
    this.activePage = 1;
    this.pages = [];
  }

  ngOnInit() {
    for (let i = 1; i <= this.numberOfPages; i++) {
      this.pages.push(i);
    }
  }

  selected(newPage: number) {
    if (newPage >= 1 && newPage <= this.numberOfPages) {
      this.activePage = newPage;
      this.pageSelected.emit(this.activePage - 1);
    }
  }

  ngOnChanges(changes) {
    this.numberOfPages = changes.numberOfPages.currentValue;
    this.pages = [];
    for (let i = 1; i <= this.numberOfPages; i++) {
      this.pages.push(i);
    }
  }
}
