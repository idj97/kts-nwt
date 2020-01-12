import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  isDisplayed: boolean = false;

  constructor() { }

  showMessage(header: string, content: string) {

    if(!this.isDisplayed) {
      document.getElementById('toaster').style.height = '15%';
      document.getElementById('toaster-header').innerHTML = header;
      document.getElementById('toaster-message').innerHTML = content;

      window.setTimeout(() => this.hideMessage(), 5000);
      this.isDisplayed = true;
    }

  }

  private hideMessage() {

    if(this.isDisplayed) {
      document.getElementById('toaster').style.height = '0';
      this.isDisplayed = false;
    }

  }

}
