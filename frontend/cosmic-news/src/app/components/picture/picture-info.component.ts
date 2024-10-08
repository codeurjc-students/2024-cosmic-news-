import { Component } from '@angular/core';
import { Picture } from '../../models/picture.model';
import { Me } from '../../models/me.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PictureService } from '../../services/picture.service';
import { UserService } from '../../services/user.service';


@Component({
    selector: "picture-info",
    templateUrl: './picture-info.component.html',
    styleUrls: ['../../styles/data.css']
})
export class PictureInfoComponent {
    me: Me;
    admin: boolean;
    logged: boolean;
    id: number;
    picture: Picture;
    photoURL: string;

    constructor(activatedRoute: ActivatedRoute, private router: Router, private service: PictureService, private userService: UserService) {
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });
        service.getPicture(this.id).subscribe(
            response => {
                this.picture = response as Picture;
            },
            error => console.error(error)
        );

        service.getPicturePhoto(this.id).subscribe(
            response => {
                if (response) {
                    const blob = new Blob([response], { type: 'image/jpeg' })
                    this.photoURL = URL.createObjectURL(blob)
                }
            },
            error => {
                console.log("Error al cargar la foto")
            }
        );

        userService.me().subscribe(
            response => {
                this.me = response as Me
                this.admin = (this.me.mail == "xd");
                this.logged = true;
            },
            error => {
                this.admin = false;
                this.logged = false;
            }
        );
    }

    deletePicture(id: number | undefined) {
        this.service.deletePicture(id).subscribe(
            _ => this.router.navigate(['/pictures']),
            error => console.error("Error deleting the picture: " + error)
        );
    }

    like(id:number|undefined){
        this.service.like(id).subscribe(
            _ => {window.location.reload();},
            error => console.error("Error with like picture" + error)
        );
    }
}