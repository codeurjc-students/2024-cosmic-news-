package es.codeurjc.cosmic_news.model;
import java.sql.Blob;

import jakarta.persistence.Lob;

@jakarta.persistence.Embeddable
public class Badge {

    @Lob
    private Blob image;
    private String name;

    private int position;

    public Badge(){}

    public Badge(Blob image, String name, int position){
        this.image = image;
        this.name = name;
        this.position = position;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
