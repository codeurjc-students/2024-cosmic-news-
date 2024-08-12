package es.codeurjc.cosmic_news.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.PictureRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PictureService {

    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    UserRepository userRepository;

    public List<Picture> getAllPictures(){
        return pictureRepository.findAll();
    }

    public Page<Picture> findAll(Pageable pageable) {
        return pictureRepository.findAll(pageable);
    }

    public Page<Picture> findAllByDate(Pageable pageable) {
        return pictureRepository.findAllByOrderByDateTaken(pageable);
    }

    public Page<Picture> findAllByLikes(Pageable pageable) {
        return pictureRepository.findAllByOrderByLikes(pageable);
    }

    public Page<Picture> findAllByUserId(Long userId, int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return pictureRepository.findAllByUserId(userId, pageable);
    }

    public Picture savePicture(Picture picture){
        picture.setLikes(0);
        pictureRepository.save(picture);
        return picture;
    }

    public boolean deletePicture(Long id){
        Optional<Picture> picture = pictureRepository.findById(id);

        if (picture.isPresent()){
            for (User user: picture.get().getUsers()){
                user.removePicture(picture.get());
                userRepository.save(user);
            }
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

    public void like(Picture picture, User user){
        Optional<Picture> pic = findPictureUserById(user, picture.getId());
        if (pic.isPresent()){
            picture.setLikes(picture.getLikes()-1);
            user.removePicture(picture);
            pictureRepository.save(picture);
            userRepository.save(user);
        }else{
            picture.setLikes(picture.getLikes()+1);
            user.addPicture(picture);
            pictureRepository.save(picture);
            userRepository.save(user);
        }
    }

    public Picture findPictureById(Long id){
        Optional<Picture> picture = pictureRepository.findById(id);
        if (picture.isPresent()){
            return picture.get();
        }else{
            return null;
        }
    }

    public Optional<Picture> findPictureUserById(User user, long id) {
        return user.getPictures().stream()
                .filter(picture -> picture.getId() == id)
                .findFirst();
    }
}
