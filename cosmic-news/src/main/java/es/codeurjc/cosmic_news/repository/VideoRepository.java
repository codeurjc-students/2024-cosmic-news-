package es.codeurjc.cosmic_news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
   
}
