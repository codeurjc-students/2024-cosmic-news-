package es.codeurjc.cosmic_news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.codeurjc.cosmic_news.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n ORDER BY n.likes DESC")
    public Page<News> findAllByOrderByLikes(Pageable pageable);
    @Query("SELECT n FROM News n ORDER BY n.date DESC")
    public Page<News> findAllByOrderByDate(Pageable pageable);
    public Page<News> findAllByOrderByReadingTime(Pageable pageable);

    @Query("SELECT p FROM News p JOIN p.users u WHERE u.id = :userId")
    Page<News> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}

