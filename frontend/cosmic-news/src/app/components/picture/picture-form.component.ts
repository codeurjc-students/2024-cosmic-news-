import { Component, ViewChild } from '@angular/core';
import { Picture } from '../../models/picture.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { PictureService } from '../../services/picture.service';
import { Observable } from 'rxjs';

@Component({
    selector: "picture-form",
    templateUrl: './picture-form.component.html',
    styleUrl: '../../styles/form.css'
})
export class PictureFormComponent{
    picture:Picture;
    edit:boolean;

    @ViewChild("file")
    file:any;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: PictureService, private httpClient: HttpClient) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.picture = { }
        if (id){
            this.edit = true;

        }

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id) {
            service.getPicture(id).subscribe(
                picture => {
                    this.picture = picture
                    this.edit = true;
                },
                error => console.error(error)
            );
        }
    }

    save() {
        this.service.addOrUpdatePicture(this.picture).subscribe(
            response => {
                this.picture = response as Picture;
                this.uploadPictureImage(this.picture).subscribe(
                    _ => {
                        this.router.navigate(['/pictures', this.picture.id]);
                    },
                    error => {
                        alert('Error uploading picture image: ' + error);
                        this.router.navigate(['/pictures', this.picture.id]);
                    }
                );
            },
            error => {
                console.error('Error creating/updating the picture: ' + error)
                this.router.navigate(['/pictures'])
            }
        );
    }


    private uploadPictureImage(picture: Picture) {
        const image = this.file.nativeElement.files[0];

        if (!image)
            return new Observable(observer => {
                observer.next();
                observer.complete();
            });

        let formData = new FormData();
        formData.append("imageFile", image);

        return this.service.setPicturePhoto(picture, formData)
    }

}