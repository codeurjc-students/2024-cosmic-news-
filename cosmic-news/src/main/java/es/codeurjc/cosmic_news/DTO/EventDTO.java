package es.codeurjc.cosmic_news.DTO;

import java.time.LocalDate;

import es.codeurjc.cosmic_news.model.Event;

public class EventDTO {
    private Long id;
    private LocalDate date;
    private String icon;
    private String description;

    public EventDTO(){}

    public EventDTO(Event event){
        this.id = event.getId();
        this.date = event.getDate();
        this.icon = event.getIcon();
        this.description = event.getDescription();
    }

    public Event toEvent(){
        Event event = new Event();
        event.setDate(this.date);
        event.setIcon(this.icon);
        event.setDescription(this.description);
        return event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
