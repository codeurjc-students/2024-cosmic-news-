import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { News } from '../models/news.model';
import { Picture } from '../models/picture.model';
import { Video } from '../models/video.model';
import { User } from '../models/user.model';



@Injectable({
  providedIn: 'root'
})
export class PaginationService {

  private newsURL = '/api/news';
  private picturesURL = '/api/pictures';
  private videosURL = '/api/videos';
  private quizzesURL = '/api/quizzes';

  constructor(private http: HttpClient) { }

  getNews(page: number, size: number, filter: string): Observable<News[]> {
    const params = { page: page.toString(), size: size.toString(), filter: filter };
    return this.http.get<News[]>(this.newsURL, { params })
  }

  getPictures(page: number, size: number, filter: string): Observable<Picture[]> {
    const params = { page: page.toString(), size: size.toString(), filter: filter };
    return this.http.get<Picture[]>(this.picturesURL, { params });
  }

  getVideos(page: number, size: number): Observable<Video[]> {
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<Video[]>(this.videosURL, { params })
  }

/*  getQuizzes(page: number, size: number): Observable<Quizz[]> {
    const params = { page: page.toString(), size: size.toString() };
    return this.http.get<Quizz[]>(this.quizzesURL, { params })
  }*/

  getNewsImage(news: News): Observable<Blob> {
    const url = this.newsURL + "/" + news.id + "/image"
    return this.http.get(url, { responseType: 'blob' });
  }

  getPictureImage(picture: Picture): Observable<Blob> {
    const url = this.picturesURL + "/" + picture.id + "/image";
    return this.http.get(url, { responseType: 'blob' });
  }
}
