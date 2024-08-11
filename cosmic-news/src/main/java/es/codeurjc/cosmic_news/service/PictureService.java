package es.codeurjc.cosmic_news.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.repository.PictureRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PictureService {

    @Autowired
    PictureRepository pictureRepository;

    public List<Picture> getAllPictures(){
        return pictureRepository.findAll();
    }

    public Picture savePicture(Picture picture){
        picture.setLikes(0);
        pictureRepository.save(picture);
        return picture;
    }

    public boolean deletePicture(Long id){
        Optional<Picture> picture = pictureRepository.findById(id);

        if (picture.isPresent()){
            pictureRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public void updatePicture(Picture picture, HttpServletRequest request) {
        picture.setTitle(request.getParameter("title"));
        picture.setLocation(request.getParameter("location"));
        picture.setAuthor(request.getParameter("author"));
        picture.setDescription(request.getParameter("description"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        String dateStr = request.getParameter("dateTaken");

        try {
            date = LocalDate.parse(dateStr, formatter);
            picture.setDateTaken(date);
        } catch (DateTimeParseException e) {
        }

        pictureRepository.save(picture);
    }

    public Picture findPictureById(Long id){
        Optional<Picture> picture = pictureRepository.findById(id);
        if (picture.isPresent()){
            return picture.get();
        }else{
            return null;
        }
    }
}
