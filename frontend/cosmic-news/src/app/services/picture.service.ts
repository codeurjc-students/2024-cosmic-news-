import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Picture } from "../models/picture.model";

const urlPictures = '/api/pictures'

@Injectable({ providedIn: 'root' })
export class PictureService {
    constructor(private httpClient: HttpClient) { }

    addOrUpdatePicture(picture: Picture){
        if (!picture.id){
            return this.addPicture(picture);
        } else{
            return this.updatePicture(picture);
        }
    }

    getPicture(id: number): Observable<Object> {
        return this.httpClient.get(urlPictures + "/" + id);
    }

    getPicturePhoto(id: number) {
        return this.httpClient.get(urlPictures + "/" + id + "/image", { responseType: 'arraybuffer' });
    }

    deletePicture(id: number | undefined) {
        return this.httpClient.delete(urlPictures + "/" + id);
    }

    editPicture(id: number | undefined, picture: Picture) {
        this.httpClient.put(urlPictures + "/" + id, picture).subscribe();
        return true
    }

    newPicture(picture: Picture): Observable<Object>{
        return this.httpClient.post(urlPictures, picture);
    }

    setPicturePhoto(picture: Picture, formData: FormData) {
        return this.httpClient.post(urlPictures + "/"+ picture.id + '/image', formData)
                .pipe(
                    catchError(error => this.handleError(error))
                );
    }

    private addPicture(picture: Picture){
        return this.httpClient.post(urlPictures,picture).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updatePicture(picture:Picture){
        return this.httpClient.put(urlPictures+"/"+picture.id,picture).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}