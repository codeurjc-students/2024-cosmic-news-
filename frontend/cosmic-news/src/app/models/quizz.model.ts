import { Question } from "./question.model";

export interface Quizz{
    id?:number,
    name?:string,
    difficulty?:string,
    questions?: Question[];
    score?:number,
    badge?:string;
}