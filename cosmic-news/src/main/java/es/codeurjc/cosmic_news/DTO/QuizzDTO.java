package es.codeurjc.cosmic_news.DTO;

import es.codeurjc.cosmic_news.model.Quizz;

public class QuizzDTO {
    private Long id;
    private String name;
    private String difficulty;

    public QuizzDTO(){}

    public QuizzDTO(Quizz quizz){
        this.id = quizz.getId();
        this.name = quizz.getName();
        this.difficulty = quizz.getDifficulty();
    }

    public Quizz toQuizz(){
        Quizz quizz = new Quizz();
        quizz.setName(this.name);
        quizz.setDifficulty(this.difficulty);
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
}
