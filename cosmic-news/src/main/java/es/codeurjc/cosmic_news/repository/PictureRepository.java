package es.codeurjc.cosmic_news.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    public List<Picture> findAllByOrderByLikes();
    public List<Picture> findAllByOrderByDateTaken();
}