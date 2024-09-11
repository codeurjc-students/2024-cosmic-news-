import { Component, ViewChild } from '@angular/core';
import { News } from '../../models/news.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NewsService } from '../../services/news.service';
import { Observable } from 'rxjs';

@Component({
    selector: "news-form",
    templateUrl: './news-form.component.html',
    styleUrl: '../../styles/form.css'
})
export class NewsFormComponent{
    news:News;
    edit:boolean;

    @ViewChild("file")
    file:any;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: NewsService, private httpClient: HttpClient) {
        let id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.news = { }
        if (id){
            this.edit = true;

        }

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (id) {
            service.getNews(id).subscribe(
                news => {
                    this.news = news
                    this.edit = true;
                },
                error => console.error(error)
            );
        }
    }

    save() {
        this.service.addOrUpdateNews(this.news).subscribe(
            response => {
                this.news = response as News;
                this.uploadNewsImage(this.news).subscribe(
                    _ => {
                        this.router.navigate(['/news', this.news.id]);
                    },
                    error => {
                        alert('Error uploading news image: ' + error);
                        this.router.navigate(['/news', this.news.id]);
                    }
                );
            },
            error => {
                console.error('Error creating/updating the news: ' + error)
                this.router.navigate(['/news'])
            }
        );
    }


    private uploadNewsImage(news: News) {
        const image = this.file.nativeElement.files[0];

        if (!image)
            return new Observable(observer => {
                observer.next();
                observer.complete();
            });

        let formData = new FormData();
        formData.append("imageFile", image);

        return this.service.setNewsPhoto(news, formData)
    }

}