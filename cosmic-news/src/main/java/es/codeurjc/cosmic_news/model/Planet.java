package es.codeurjc.cosmic_news.model;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double radius;
    private double orbitRadius;
    private double orbitSpeed;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private String imageUrl;

    @JsonIgnore
    private Blob photo;

    public Planet() {}

    public Planet(String name, double radius, double orbitRadius, double orbitSpeed, String description, String imageUrl) {
        this.name = name;
        this.radius = radius;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public double getRadius() {
        return radius; 
    }
    
    public void setRadius(double radius) {
        this.radius = radius; 
    }

    public double getOrbitRadius() {
        return orbitRadius; 
    }
    public void setOrbitRadius(double orbitRadius) {
        this.orbitRadius = orbitRadius; 
    }

    public double getOrbitSpeed() {
        return orbitSpeed; 
    }
    public void setOrbitSpeed(double orbitSpeed) {
        this.orbitSpeed = orbitSpeed; 
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getImageUrl(){ 
        return imageUrl; 
    }
    public void setImageUrl(String imageUrl){ 
        this.imageUrl = imageUrl; 
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }
}