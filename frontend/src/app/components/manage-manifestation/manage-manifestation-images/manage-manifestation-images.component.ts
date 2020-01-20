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

  selectedFile: any; // image selected after browse
  imageName: string; // name displayed in the browse input
  imgUrl: any; // image displayed in the modal

  @Input() imagesToUpload: Array<any>;
  @Input() uploadedImages: Array<any>;

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

    if(!this.selectedFile) {
      this.toastService.showMessage('Adding failed', 'Please select an image before adding it');
      return;
    }

    if(this.isImageAdded(this.selectedFile.name)) {
      this.toastService.showMessage('Adding failed', 'The image is already added');
      return;
    }

    // converting file size to MB
    if((this.selectedFile.size / 1024)/1024 > 1.0) {
      this.toastService.showMessage('Adding failed', 'The selected image is too large. Please limit image size to 1 MB.');
      return;
    }

    this.imagesToUpload.push(this.selectedFile);

  }

  removeBrowsedImage(imageIndex: number) {
    this.imagesToUpload.splice(imageIndex, 1);
  }

  removeUploadedImage(imageIndex: number) {
    this.uploadedImages.splice(imageIndex, 1);
  }

  isImageAdded(imageName: string): boolean {

    for(let i = 0; i < this.imagesToUpload.length; i++) {
      if(this.imagesToUpload[i].name == imageName) {
        return true;
      }
    }

    return false;

  }

  displayBrowsedImage(image: any): void {
    
    // extract the image url
    let reader = new FileReader();
    reader.readAsDataURL(image);

    reader.onload = (event) => {
      this.imgUrl = reader.result;
    } 

    this.displayImagePreview();

  }


  displayUploadedImage(image: any): void {

    this.imgUrl = `data:image/jpeg;base64,${image.image}`
    this.displayImagePreview();
    
  }

  displayImagePreview(): void {
    this.modalDisplayed = true;
  }

  closeImagePreview(): void {
    this.modalDisplayed = false;
  }

}
