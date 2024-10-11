package es.codeurjc.cosmic_news.DTO;

import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;

public class QuestionDTO {

    private Long id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;   
    private String answer;
    private int num;

    private boolean correct1;
    private boolean correct2;
    private boolean correct3;
    private boolean correct4;

    private String selected;

    public QuestionDTO(){}

    public QuestionDTO(Question question){
        this.id = question.getId();
        this.question = question.getQuestion();
        this.option1 = question.getOption1();
        this.option2 = question.getOption2();
        this.option3 = question.getOption3();
        this.option4 = question.getOption4();
        this.answer = question.getAnswer();
        this.correct1 = question.isCorrect1();
        this.correct2 = question.isCorrect2();
        this.correct3 = question.isCorrect3();
        this.correct4 = question.isCorrect4();
        this.num = question.getNum();
    }

    public Question toQuestion(Quizz quizz){
        Question question = new Question();
        question.setQuestion(this.question);
        question.setOption1(this.option1);
        question.setOption2(this.option2);
        question.setOption3(this.option3);
        question.setOption4(this.option4);
        question.setAnswer(this.answer);
        question.setCorrect1(this.correct1);
        question.setCorrect2(this.correct2);
        question.setCorrect3(this.correct3);
        question.setCorrect4(this.correct4);
        question.setQuizz(quizz);
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

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public boolean isCorrect1() {
        return correct1;
    }

    public void setCorrect1(boolean correct1) {
        this.correct1 = correct1;
    }

    public boolean isCorrect2() {
        return correct2;
    }

    public void setCorrect2(boolean correct2) {
        this.correct2 = correct2;
    }

    public boolean isCorrect3() {
        return correct3;
    }

    public void setCorrect3(boolean correct3) {
        this.correct3 = correct3;
    }

    public boolean isCorrect4() {
        return correct4;
    }

    public void setCorrect4(boolean correct4) {
        this.correct4 = correct4;
    }

    public String getSelected(){
        return selected;
    }

    public void setSelected(String selected){
        this.selected = selected;
    }

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;
    }
}
