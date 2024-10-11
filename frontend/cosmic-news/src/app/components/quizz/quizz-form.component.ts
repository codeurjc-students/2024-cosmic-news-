import { Component, ViewChild } from '@angular/core';
import { Quizz } from '../../models/quizz.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { QuizzService } from '../../services/quizz.service';
import { Observable } from 'rxjs';

@Component({
  selector: "quizz-form",
  templateUrl: './quizz-form.component.html',
  styleUrls: ['../../styles/form.css', '../../styles/quizz.css']
})
export class QuizzFormComponent {
  quizz: Quizz = { name: '', difficulty: 'Normal', questions: [] };
  edit: boolean = false;

  @ViewChild("file") file: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private service: QuizzService,
    private httpClient: HttpClient
  ) {
    const id = activatedRoute.snapshot.params['id'];
    if (id) {
      this.loadQuizz(id);
    } else {
      this.addQuestion();  // Agregar una pregunta inicial si no está en modo edición
    }
  }

  // Método para cargar el quizz en modo edición
  loadQuizz(id: number): void {
    this.service.getQuizz(id).subscribe(
      quizz => {
        this.quizz = quizz;
        this.edit = true;
        this.mapCorrectAnswers();
      },
      error => console.error(error)
    );
  }

  // Mapear la respuesta correcta con los valores de 'option1', 'option2', etc.
  private mapCorrectAnswers(): void {
    this.quizz.questions?.forEach(question => {
      switch (question.answer) {
        case question.option1:
          question.answer = "option1";
          break;
        case question.option2:
          question.answer = "option2";
          break;
        case question.option3:
          question.answer = "option3";
          break;
        case question.option4:
          question.answer = "option4";
          break;
      }
    });
  }

  // Agregar una nueva pregunta al quizz
  addQuestion(): void {
    const questionIndex = (this.quizz.questions?.length || 0) + 1;
    this.quizz.questions?.push({
      num: questionIndex,
      question: '',
      option1: '',
      option2: '',
      option3: '',
      option4: '',
      answer: 'option1'
    });
  }

  // Guardar el quizz
  save(): void {
    this.quizz.questions?.forEach(question => {
      switch (question.answer) {
        case "option1":
          question.answer = question.option1;
          break;
        case "option2":
          question.answer = question.option2;
          break;
        case "option3":
          question.answer = question.option3;
          break;
        case "option4":
          question.answer = question.option4;
          break;
      }
    });

    this.service.addOrUpdateQuizz(this.quizz).subscribe(
      response => {
        this.quizz = response as Quizz;
        this.uploadBadge(this.quizz).subscribe(
          _ => this.router.navigate(['/quizzes', this.quizz.id]),
          error => {
            alert('Error uploading badge image: ' + error);
            this.router.navigate(['/quizzes', this.quizz.id]);
          }
        );
      },
      error => {
        console.error('Error creating/updating the quiz: ' + error);
        this.router.navigate(['/quizzes']);
      }
    );
  }

  // Subir la imagen de la insignia
  private uploadBadge(picture: Quizz): Observable<any> {
    const image = this.file.nativeElement.files[0];
    if (!image) {
      return new Observable(observer => {
        observer.next();
        observer.complete();
      });
    }

    let formData = new FormData();
    formData.append("imageFile", image);

    return this.service.setBadge(picture, formData);
  }
}