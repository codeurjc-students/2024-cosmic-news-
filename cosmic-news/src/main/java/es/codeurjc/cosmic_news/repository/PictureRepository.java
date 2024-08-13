package es.codeurjc.cosmic_news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.codeurjc.cosmic_news.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Query("SELECT n FROM Picture n ORDER BY n.likes DESC")
    public Page<Picture> findAllByOrderByLikes(Pageable pageable);
    @Query("SELECT n FROM Picture n ORDER BY n.dateTaken DESC")
    public Page<Picture> findAllByOrderByDateTaken(Pageable pageable);

    @Query("SELECT p FROM Picture p JOIN p.users u WHERE u.id = :userId")
    Page<Picture> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}