package es.codeurjc.cosmic_news.repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
    public List<News> findAllByOrderByLikes();
    public List<News> findAllByOrderByDate();
    public List<News> findAllByOrderByReadingTime();
}

