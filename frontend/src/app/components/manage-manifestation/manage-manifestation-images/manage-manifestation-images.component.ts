import { Component, OnInit, Input } from '@angular/core';
import { ManifestationService } from 'src/app/services/manifestation.service';
import { ToasterService } from 'src/app/services/toaster.service';
import { ManifestationSection } from 'src/app/models/manifestation-section.model';
import { ManifestationImage } from 'src/app/models/manifestation-image-model';

@Component({
  selector: 'app-manage-manifestation-images',
  templateUrl: './manage-manifestation-images.component.html',
  styleUrls: ['./manage-manifestation-images.component.css']
})
export class ManageManifestationImagesComponent implements OnInit {

  modalDisplayed: boolean;

  selectedFile: any;
  imageName: string;
  imgUrl: any;

  @Input() manifestationImages: Array<any>;

  constructor(private toastService: ToasterService) {}

  ngOnInit() {
    this.modalDisplayed = false;
    this.imageName = 'Choose image'; //default label
  }

  onImageChanged(event): void {
    this.selectedFile = event.target.files[0];

    // fitting the name in the input
    if(this.selectedFile.name.length > 28) {
      this.imageName = this.selectedFile.name.substr(0, 28);
    } else {
      this.imageName = this.selectedFile.name;
    }
    
  }

  addImage(): void {

    if(this.isImageAdded(this.selectedFile.name)) {
      this.toastService.showMessage('Fail', 'The image is already added');
      return;
    }

    this.manifestationImages.push(this.selectedFile);

  }

  removeImage(imageIndex: number) {
    this.manifestationImages.splice(imageIndex, 1);
  }

  isImageAdded(imageName: string): boolean {

    for(let i = 0; i < this.manifestationImages.length; i++) {
      if(this.manifestationImages[i].name == imageName) {
        return true;
      }
    }

    return false;

  }

  displayImage(image: any): void {
    
    // extract the image url
    let reader = new FileReader();
    reader.readAsDataURL(image);

    reader.onload = (event) => {
      this.imgUrl = reader.result;
    } 

    this.displayImagePreview();

  }

  displayImagePreview(): void {
    this.modalDisplayed = true;
  }

  closeImagePreview(): void {
    this.modalDisplayed = false;
  }

}
