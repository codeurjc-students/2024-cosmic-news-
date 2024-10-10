package es.codeurjc.cosmic_news.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;

    @JsonIgnore
    private boolean select1;
    @JsonIgnore
    private boolean select2;
    @JsonIgnore
    private boolean select3;
    @JsonIgnore
    private boolean select4;

    @JsonIgnore
    private boolean correct1;
    @JsonIgnore
    private boolean correct2;
    @JsonIgnore
    private boolean correct3;
    @JsonIgnore
    private boolean correct4;

    private int num;

    @ManyToOne
    @JsonIgnore
    private Quizz quizz;

    public Question(){}

    public Question(String question, String option1, String option2, String option3, String option4, String answer, int num, Quizz quizz){
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.num = num;
        this.quizz = quizz;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setQuizz(Quizz quizz){
        this.quizz = quizz;
    }

    public Quizz getQuizz(){
        return quizz;
    }

    public boolean isSelect1() {
        return select1;
    }

    public void setSelect1(boolean select1) {
        this.select1 = select1;
    }

    public boolean isSelect2() {
        return select2;
    }

    public void setSelect2(boolean select2) {
        this.select2 = select2;
    }

    public boolean isSelect3() {
        return select3;
    }

    public void setSelect3(boolean select3) {
        this.select3 = select3;
    }

    public boolean isSelect4() {
        return select4;
    }

    public void setSelect4(boolean select4) {
        this.select4 = select4;
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
}
