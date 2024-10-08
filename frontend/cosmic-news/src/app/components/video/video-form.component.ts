import { Component, ViewChild } from '@angular/core';
import { Video } from '../../models/video.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { VideoService } from '../../services/video.service';
import { Observable } from 'rxjs';

@Component({
    selector: "video-form",
    templateUrl: './video-form.component.html',
    styleUrl: '../../styles/form.css'
})
export class VideoFormComponent{
    video:Video;
    edit:boolean;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: VideoService, private httpClient: HttpClient) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.video = { }
        if (id){
            this.edit = true;

        }

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id) {
            service.getVideo(id).subscribe(
                video => {
                    this.video = video
                    this.edit = true;
                },
                error => console.error(error)
            );
        }
    }

    save() {
        this.service.addOrUpdateVideo(this.video).subscribe(
            response => {
                this.video = response as Video;
                this.router.navigate(['/videos', this.video.id]);
            },
            error => {
                console.error('Error creating/updating the video: ' + error)
                this.router.navigate(['/videos'])
            }
        );
    }

}