package es.codeurjc.cosmic_news.DTO;

import es.codeurjc.cosmic_news.model.Question;

public class QuestionDTO {

    private Long id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    public QuestionDTO(){}

    public QuestionDTO(Question question){
        this.id = question.getId();
        this.question = question.getQuestion();
        this.option1 = question.getOption1();
        this.option2 = question.getOption2();
        this.option3 = question.getOption3();
        this.option4 = question.getOption4();
    }

    public Question toQuestion(){
        Question question = new Question();
        question.setQuestion(this.question);
        question.setOption1(this.option1);
        question.setOption2(this.option2);
        question.setOption3(this.option3);
        question.setOption4(this.option4);
        return question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }
}
