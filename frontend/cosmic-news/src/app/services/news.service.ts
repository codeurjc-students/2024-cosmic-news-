import { Injectable } from "@angular/core";
import { Observable, throwError, catchError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { News } from "../models/news.model";

const urlNews = '/api/news'

@Injectable({ providedIn: 'root' })
export class NewsService {
    constructor(private httpClient: HttpClient) { }

    addOrUpdateNews(news: News){
        if (!news.id){
            return this.addNews(news);
        } else{
            return this.updateNews(news);
        }
    }

    getNews(id: number): Observable<Object> {
        return this.httpClient.get(urlNews + "/" + id);
    }

    getNewsPhoto(id: number) {
        return this.httpClient.get(urlNews + "/" + id + "/image", { responseType: 'arraybuffer' });
    }

    deleteNews(id: number | undefined) {
        return this.httpClient.delete(urlNews + "/" + id);
    }

    editNews(id: number | undefined, news: News) {
        this.httpClient.put(urlNews + "/" + id, news).subscribe();
        return true
    }

    newNews(news: News): Observable<Object>{
        return this.httpClient.post(urlNews, news);
    }

    like(id:number | undefined){
        return this.httpClient.post(urlNews+"/"+id+"/like",null);
    }

    setNewsPhoto(news: News, formData: FormData) {
        return this.httpClient.post(urlNews + "/"+ news.id + '/image', formData)
                .pipe(
                    catchError(error => this.handleError(error))
                );
    }

    private addNews(news: News){
        return this.httpClient.post(urlNews,news).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateNews(news:News){
        return this.httpClient.put(urlNews+"/"+news.id,news).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}