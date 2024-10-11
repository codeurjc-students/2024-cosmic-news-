import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { PaginationService } from '../../services/pagination.service';
import { Quizz } from '../../models/quizz.model';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';
import { ChartType } from 'angular-google-charts';

@Component({
  selector: "quizzes",
  templateUrl: './quizzes.component.html',
  styleUrls: ['../../styles/cards.css', '../../styles/quizzes.css']
})
export class QuizzesComponent implements OnInit {
  quizzes: Quizz[] = [];
  last_page: number = 0;
  hasMore: boolean = true;
  cardWidth: number = 250 + 2 * 20;
  rowElements: number;
  canAdd: boolean = false;
  me: Me;
  
  data: (string | number)[][] = [];
  options = {
    title: 'Distribución de Estadísticas',
    is3D: true,
    legend: { position: 'bottom' },
    tooltip: { showColorCode: true, isHtml: true },
    pieSliceTextStyle: { fontSize: 14 }
  };
  
  chartType: ChartType = ChartType.PieChart;

  constructor(
    private service: PaginationService,
    private userService: UserService,
    private elementRef: ElementRef
  ) {}

  ngOnInit(): void {
    this.updateRowElements();
    this.loadQuizzes();
    this.checkUser();
    
    this.service.getPieChart().subscribe(
      response => {
        if (response && response instanceof Map) {
          this.data = Array.from(response.entries());
        } else {
          console.error("Respuesta null, undefined o no es un mapa");
        }
      },
      error => console.error("Error al obtener los datos de la gráfica", error)
    );
  }

  private checkUser() {
    this.userService.me().subscribe(
      response => {
        this.me = response as Me;
        this.canAdd = (this.me.mail === "xd"); // Check the admin
      },
      _error => console.log("Error al obtener el usuario")
    );
  }

  loadQuizzes() {
    if (this.last_page === 0) {
      this.quizzes = [];
    }

    this.service.getQuizzes(this.last_page, this.rowElements).subscribe(
      (quizzes: Quizz[]) => {
        if (!quizzes) {
          this.hasMore = false;
          return;
        }

        quizzes.forEach(quizz => {
          this.service.getBadge(quizz).subscribe(
            (image) => {
              quizz.badge = URL.createObjectURL(image);
            },
            (error) => {
              console.log(error);
            }
          );
        });

        this.quizzes.push(...quizzes);
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