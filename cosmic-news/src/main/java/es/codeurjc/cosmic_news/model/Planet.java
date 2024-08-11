package es.codeurjc.cosmic_news.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.sql.Blob;

@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double radius;
    private String color; // Puedes almacenar el color como un String en formato hexadecimal
    private double orbitRadius;
    private double orbitSpeed;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob image; // Imagen almacenada como BLOB

    // Constructor vacío
    public Planet() {}

    // Constructor con parámetros
    public Planet(String name, double radius, String color, double orbitRadius, double orbitSpeed, Blob image) {
        this.name = name;
        this.radius = radius;
        this.color = color;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.image = image;
    }

    // Getters y setters
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

    public String getColor() {
        return color; 
    }

    public void setColor(String color) {
        this.color = color; 
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

    public Blob getImage() { 
        return image; 
    }
    public void setImage(Blob image) {
         this.image = image; 
    }
}