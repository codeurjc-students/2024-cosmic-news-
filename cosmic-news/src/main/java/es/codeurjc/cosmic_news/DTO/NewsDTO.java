package es.codeurjc.cosmic_news.DTO;

import java.time.LocalDate;

import es.codeurjc.cosmic_news.model.News;

public class NewsDTO {

    private Long id;
    private String title;
    private String subtitle;
    private String author;
    private String topic;
    private String bodyText;
    private int readingTime;
    private LocalDate date;   
    private int likes;

    public NewsDTO(){}

    public NewsDTO(News news){
        this.id = news.getId();
        this.title = news.getTitle();
        this.subtitle = news.getSubtitle();
        this.author = news.getSubtitle();
        this.topic = news.getTopic();
        this.bodyText = news.getBodyText();
        this.readingTime = news.getReadingTime();
        this.date = news.getDate();
        this.likes = news.getLikes();
    }

    public News toNews(){
        News news = new News();
        news.setTitle(this.title);
        news.setSubtitle(this.subtitle);
        news.setAuthor(this.author);
        news.setTopic(this.topic);
        news.setBodyText(this.bodyText);
        news.setReadingTime(this.readingTime);
        news.setDate(this.date);
        news.setLikes(this.likes);
        return news;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public int getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(int readingTime) {
        this.readingTime = readingTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
