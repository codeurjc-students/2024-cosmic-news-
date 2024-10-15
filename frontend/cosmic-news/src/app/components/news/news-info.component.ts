import { Component } from '@angular/core';
import { News } from '../../models/news.model';
import { Me } from '../../models/me.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NewsService } from '../../services/news.service';
import { UserService } from '../../services/user.service';


@Component({
    selector: "news-info",
    templateUrl: './news-info.component.html',
    styleUrls: ['../../styles/data.css']
})
export class NewsInfoComponent {
    me: Me;
    admin: boolean;
    logged: boolean;
    id: number;
    news: News;
    photoURL: string;

    constructor(activatedRoute: ActivatedRoute, private router: Router, private service: NewsService, private userService: UserService) {
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });
        service.getNews(this.id).subscribe(
            response => {
                this.news = response as News;
            },
            error => console.error(error)
        );

        service.getNewsPhoto(this.id).subscribe(
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

    deleteNews(id: number | undefined) {
        this.service.deleteNews(id).subscribe(
            _ => this.router.navigate(['/news']),
            error => console.error("Error deleting the news: " + error)
        );
    }

    like(id:number|undefined){
        if (this.logged){
            this.service.like(id).subscribe(
                _ => {window.location.reload();},
                error => console.error("Error with like news" + error)
            );
        }else{
            this.router.navigate(['/login']);
        }
    }
}