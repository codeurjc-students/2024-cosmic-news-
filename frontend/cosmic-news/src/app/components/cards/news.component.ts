import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { News } from '../../models/news.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: "news",
  templateUrl: './news.component.html',
  styleUrls: ['./cards.css' , '../../../styles.css']
})
export class NewsComponent implements OnInit {
  newsList: News[] = [];
  last_page: number = 0;
  hasMore: boolean = true;
  cardWidth: number = 250 + 2 * 20;
  rowElements: number;
  canAdd: boolean = false;
  me: Me;

  constructor(
    private service: PaginationService,
    private userService: UserService,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.updateRowElements();
    this.loadNews();
    this.checkUser();
  }

  private checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
      },
      _error => console.log("Error al obtener el usuario")
    );
  }

  loadNews() {
    this.service.getNews(this.last_page, this.rowElements).subscribe(
      (newsList: News[]) => {
        if (!newsList) {
          this.hasMore = false;
          return;
        }

        for (let news of newsList) {
          this.service.getNewsImage(news).subscribe(
            (image) => {
              news.image = URL.createObjectURL(image);
            },
            (error) => {
              console.log(error);
            }
          );
        }

        this.newsList.push(...newsList);
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );

    this.last_page++;

    this.service.getNews(this.last_page, this.rowElements).subscribe(
      (newsList: News[]) => {
        if (!newsList)
          this.hasMore = false;
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
  }

  private updateRowElements() {
    const cards = this.elementRef.nativeElement.querySelector('.cards');
    const total = Math.floor(cards.offsetWidth / this.cardWidth);
    this.rowElements = Math.max(2, Math.min(5, total));
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.updateRowElements();
  }
}