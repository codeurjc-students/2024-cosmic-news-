package es.codeurjc.cosmic_news.DTO;

import java.time.LocalDate;

import es.codeurjc.cosmic_news.model.Picture;

public class PictureDTO {
    private Long id;
    private String title;
    private String author;
    private String location;
    private String description;
    private LocalDate dateTaken;
    private int likes;

    public PictureDTO(){}

    public PictureDTO(Picture picture){
        this.id = picture.getId();
        this.title = picture.getTitle();
        this.author = picture.getAuthor();
        this.location = picture.getLocation();
        this.description = picture.getDescription();
        this.dateTaken = picture.getDateTaken();
        this.likes = picture.getLikes();
    }

    public Picture toPicture(){
        Picture picture = new Picture();
        picture.setTitle(this.title);
        picture.setAuthor(this.author);
        picture.setLocation(this.location);
        picture.setDescription(this.description);
        picture.setDateTaken(this.dateTaken);
        return picture;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDate dateTaken) {
        this.dateTaken = dateTaken;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
