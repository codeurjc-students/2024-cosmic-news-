package es.codeurjc.cosmic_news.DTO;

import es.codeurjc.cosmic_news.model.Video;

public class VideoDTO {

    private Long id;
    private String title;
    private String duration;
    private String description;
    private String videoUrl;

    public VideoDTO(){}

    public VideoDTO(Video video){
        this.id = video.getId();
        this.title = video.getTitle();
        this.duration = video.getDuration();
        this.description = video.getDescription();
        this.videoUrl = video.getVideoUrl();
    }

    public Video toVideo(){
        Video video = new Video();
        video.setTitle(this.title);
        video.setDuration(this.duration);
        video.setDescription(this.description);
        video.setVideoUrl(this.videoUrl);
        return video;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
