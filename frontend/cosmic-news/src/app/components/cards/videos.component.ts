import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Video } from '../../models/video.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: "videos",
  templateUrl: './videos.component.html',
  styleUrl: '../../styles/cards.css'
})
export class VideosComponent implements OnInit {
  videos: Video[] = [];
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
    this.loadVideos();
    this.checkUser();
  }

  private checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        this.canAdd = (this.me.mail == "xd"); //Check the admin
      },
      _error => console.log("Error al obtener el usuario")
    );
  }

  loadVideos() {
    if (this.last_page === 0) {
      this.videos = [];
    }

    this.service.getVideos(this.last_page, this.rowElements).subscribe(
      (videos: Video[]) => {
        if (!videos) {
          this.hasMore = false;
          return;
        }

        this.videos.push(...videos);
        this.last_page++;
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