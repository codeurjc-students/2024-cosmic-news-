import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Picture } from '../../models/picture.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';

@Component({
  selector: "pictures",
  templateUrl: './pictures.component.html',
  styleUrl: '../../styles/cards.css'
})
export class PicturesComponent implements OnInit {
  pictures: Picture[] = [];
  filter: string = "date";
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
    this.loadPictures();
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

  loadPictures() {
    if (this.last_page === 0) {
      this.pictures = [];
    }

    this.service.getPictures(this.last_page, this.rowElements, this.filter).subscribe(
      (pictures: Picture[]) => {
        if (!pictures) {
          this.hasMore = false;
          return;
        }

        pictures.forEach(picture => {
          this.service.getPictureImage(picture).subscribe(
            (image) => {
              picture.image = URL.createObjectURL(image);
            },
            (error) => {
              console.log(error);
            }
          );
        });

        this.pictures.push(...pictures);
        this.last_page++;
      },
      (error) => {
        console.log(error);
        this.hasMore = false;
      }
    );
  }

  onFilterChange(filter: string) {
    this.filter = filter;
    this.last_page = 0; 
    this.hasMore = true;
    this.loadPictures();
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