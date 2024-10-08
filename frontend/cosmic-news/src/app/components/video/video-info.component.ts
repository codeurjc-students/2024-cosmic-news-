import { Component } from '@angular/core';
import { Video } from '../../models/video.model';
import { Me } from '../../models/me.model';
import { ActivatedRoute, Router } from '@angular/router';
import { VideoService } from '../../services/video.service';
import { UserService } from '../../services/user.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';


@Component({
    selector: "video-info",
    templateUrl: './video-info.component.html',
    styleUrls: ['../../styles/data.css', '../../styles/video.css']
})
export class VideoInfoComponent {
    me: Me;
    admin: boolean;
    logged: boolean;
    id: number;
    video: Video;
    videoUrl: SafeResourceUrl;

    constructor(activatedRoute: ActivatedRoute, private router: Router, private service: VideoService, private userService: UserService, private sanitizer: DomSanitizer) {
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });
        service.getVideo(this.id).subscribe(
            response => {
                this.video = response as Video;
                const videoID = this.video.videoUrl;
                this.videoUrl = this.getSafeUrl(videoID);
            },
            error => console.error(error)
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

    getSafeUrl(videoID: string | undefined): SafeResourceUrl {
        const url = `https://www.youtube.com/embed/${videoID}`;
        return this.sanitizer.bypassSecurityTrustResourceUrl(url);
    }

    deleteVideo(id: number | undefined) {
        this.service.deleteVideo(id).subscribe(
            _ => this.router.navigate(['/videos']),
            error => console.error("Error deleting the video: " + error)
        );
    }
}