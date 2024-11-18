import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { QuizzService } from "../../services/quizz.service";
import { Quizz } from "../../models/quizz.model";

@Component({
    selector: 'review',
    templateUrl: './review.component.html',
    styleUrls: ['../../styles/form.css', '../../styles/review.css']
  })

export class ReviewComponent{
    id: number;
    quizz: Quizz;

    constructor(private activatedRoute: ActivatedRoute, private router: Router) {
        activatedRoute.params.subscribe(params => {
            this.id = params['id'];
        });

        const navigation = this.router.getCurrentNavigation();
        this.quizz = navigation?.extras?.state?.['quizz'];
    }

}