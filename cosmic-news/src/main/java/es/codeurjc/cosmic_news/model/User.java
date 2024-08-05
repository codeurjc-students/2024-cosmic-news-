package es.codeurjc.cosmic_news.model;

import java.sql.Blob;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Generated;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    //@Lob
    private Blob photo;
    private boolean image;

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

     // Getters and Setters
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

    public List<String> getRoles() {
		return roles;
	}

    public void setRoles(String... roles) {
		this.roles = List.of(roles);
	}
}
