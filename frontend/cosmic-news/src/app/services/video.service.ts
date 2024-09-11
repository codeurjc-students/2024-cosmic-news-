import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Video } from "../models/video.model";

const urlVideos = '/api/videos'

@Injectable({ providedIn: 'root' })
export class VideoService {
    constructor(private httpClient: HttpClient) { }

    addOrUpdateVideo(video: Video){
        if (!video.id){
            return this.addVideo(video);
        } else{
            return this.updateVideo(video);
        }
    }

    getVideo(id: number): Observable<Object> {
        return this.httpClient.get(urlVideos + "/" + id);
    }

    deleteVideo(id: number | undefined) {
        return this.httpClient.delete(urlVideos + "/" + id);
    }

    editVideo(id: number | undefined, video: Video) {
        this.httpClient.put(urlVideos + "/" + id, video).subscribe();
        return true
    }

    newVideo(video: Video): Observable<Object>{
        return this.httpClient.post(urlVideos, video);
    }

    private addVideo(video: Video){
        return this.httpClient.post(urlVideos,video).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateVideo(video:Video){
        return this.httpClient.put(urlVideos+"/"+video.id,video).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}