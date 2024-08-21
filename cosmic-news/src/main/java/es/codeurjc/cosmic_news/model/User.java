package es.codeurjc.cosmic_news.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String surname;
    private String description;
    private String nick;
    private String mail;

    @JsonIgnore
    private String pass;

    @JsonIgnore
    private Blob photo;
    private boolean image;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    @Lob
    private List<Badge> badges;

    @ManyToMany
    @JoinTable(
        name = "user_event",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "user_news",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    @JsonIgnore
    private Set<News> news = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "user_picture",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "picture_id")
    )
    @JsonIgnore
    private Set<Picture> pictures = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User(){}

    public User(String name, String surname, String description, String nick, String mail, String pass, String... roles){
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.nick = nick;
        this.mail = mail;
        this.pass = pass;
        this.roles = List.of(roles);
    }

    public boolean isAdmin(){
        return hasRole("ADMIN");
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public void addBadge(Badge badge) {
        if (this.badges == null) {
            this.badges = new ArrayList<>();
        }
        this.badges.add(badge);
    }

    public boolean hasBadge(String badgeName) {
        if (this.badges == null) {
            return false;
        }
        for (Badge badge : this.badges) {
            if (badge.getName().equals(badgeName)) {
                return true;
            }
        }
        return false;
    }


    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.getUsers().add(this); 
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.getUsers().remove(this);
    }

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public void addNews(News n) {
        news.add(n);
        n.getUsers().add(this); 
    }

    public void removeNews(News n) {
        news.remove(n);
        n.getUsers().remove(this);
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
        picture.getUsers().add(this); 
    }

    public void removePicture(Picture picture) {
        pictures.remove(picture);
        picture.getUsers().remove(this);
    }

    public List<String> getRoles() {
		return roles;
	}

    public void setRoles(String... roles) {
		this.roles = List.of(roles);
	}
}
