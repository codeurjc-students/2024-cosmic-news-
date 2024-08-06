package es.codeurjc.cosmic_news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.Quizz;

public interface QuizzRepository extends JpaRepository<Quizz, Long> {
    public Optional<Quizz> findByName(String name);
}