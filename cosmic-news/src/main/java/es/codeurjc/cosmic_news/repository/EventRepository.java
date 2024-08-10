package es.codeurjc.cosmic_news.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    public Optional<Event> findByDate(LocalDate date);
}

