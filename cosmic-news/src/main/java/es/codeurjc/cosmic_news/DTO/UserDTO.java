package es.codeurjc.cosmic_news.DTO;

import es.codeurjc.cosmic_news.model.User;

public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String description;
    private String mail;
    private String pass;
    private String nick;

    public UserDTO(){}

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.description = user.getDescription();
        this.mail = user.getMail();
        this.pass = user.getPass();
        this.nick = user.getNick();
    }

    public User toUser(){
        User user = new User();
        user.setName(this.name);
        user.setSurname(this.surname);
        user.setDescription(this.description);
        user.setMail(this.mail);
        user.setPass(this.pass);
        user.setNick(this.nick);
        return user;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
