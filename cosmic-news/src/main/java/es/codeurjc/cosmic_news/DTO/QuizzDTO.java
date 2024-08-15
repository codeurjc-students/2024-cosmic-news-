package es.codeurjc.cosmic_news.DTO;

import java.util.ArrayList;
import java.util.List;

import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;

public class QuizzDTO {
    private Long id;
    private String name;
    private String difficulty;

    private List<QuestionDTO> questions;

    public QuizzDTO(){}

    public QuizzDTO(Quizz quizz){
        this.id = quizz.getId();
        this.name = quizz.getName();
        this.difficulty = quizz.getDifficulty();
        this.questions = new ArrayList<>();
        for (Question question: quizz.getQuestions()){
            this.questions.add(new QuestionDTO(question));
        }
    }

    public Quizz toQuizz(){
        Quizz quizz = new Quizz();
        quizz.setName(this.name);
        quizz.setDifficulty(this.difficulty);
        List<Question> questionList = new ArrayList<>();
        for (QuestionDTO questionDTO: this.questions){
            questionList.add(questionDTO.toQuestion(quizz));
        }
        quizz.setQuestions(questionList);
        return quizz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
