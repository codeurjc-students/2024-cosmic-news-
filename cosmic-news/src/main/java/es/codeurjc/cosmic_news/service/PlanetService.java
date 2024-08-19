package es.codeurjc.cosmic_news.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Planet;
import es.codeurjc.cosmic_news.repository.PlanetRepository;

@Service
public class PlanetService {
    @Autowired
    private PlanetRepository planetRepository;

    public List<Planet> getAllPlanets(){
        return planetRepository.findAll();
    }

    public Planet findPlanetById(Long id){
        Optional<Planet> planet = planetRepository.findById(id);
        if (planet.isPresent()){
            return planet.get();
        }else{
            return null;
        }
    }
}
