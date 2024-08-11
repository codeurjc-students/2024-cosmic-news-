package es.codeurjc.cosmic_news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.Planet;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
    
}
