import { Component } from "@angular/core";
import { Quizz } from "../../models/quizz.model";
import { ActivatedRoute, NavigationExtras, Router } from "@angular/router";
import { QuizzService } from "../../services/quizz.service";


@Component({
    selector: 'result',
    templateUrl: './result.component.html',
    styleUrls: ['../../styles/quiz.css','../../styles/form.css']
  })
  
export class ResultComponent{
    id: number;
    quizz: Quizz;
    completed:boolean;
    badgeURL: string;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private service: QuizzService) {
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });

        const navigation = this.router.getCurrentNavigation();
        this.quizz = navigation?.extras?.state?.['quizz'];
        this.completed = this.quizz.score === this.quizz.questions?.length

        service.getBadge(this.id).subscribe(
            response => {
                if (response) {
                    const blob = new Blob([response], { type: 'image/jpeg' })
                    this.badgeURL = URL.createObjectURL(blob)
                }
            },
            error => {
                console.log("Error al cargar la foto")
            }
        );
    }

    submit(){
        const navigationExtras: NavigationExtras = {state: {quizz: this.quizz }};
        this.router.navigate(['review'], {relativeTo: this.activatedRoute, ...navigationExtras });
    }



}