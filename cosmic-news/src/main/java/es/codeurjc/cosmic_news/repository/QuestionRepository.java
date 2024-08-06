package es.codeurjc.cosmic_news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
}
