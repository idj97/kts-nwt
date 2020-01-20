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

  selectedFile: any;
  imageName: string;

  @Input() filesToUpload: Array<any>;

  constructor() {}

  ngOnInit() {
    this.imageName = 'Choose image'; //default label
  }

  onImageChanged(event) {
    this.selectedFile = event.target.files[0];
    this.imageName = this.selectedFile.name;
  }

  addImage() {
    this.filesToUpload.push(this.selectedFile);
  
    /*
    this.manifSvc.uploadImage(uploadData).subscribe(
      data => {
        console.log(data);
        this.toastr.showMessage('Success', 'Successful image upload');
      },
      err => {
        this.toastr.showErrorMessage(err);
      }
    )*/

  }

}
