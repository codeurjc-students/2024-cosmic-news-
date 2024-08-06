package es.codeurjc.cosmic_news.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.EventRepository;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }
    
    public Event saveEvent(Event event){
        if ((eventRepository.findByDay(event.getDay()).isPresent())&&(eventRepository.findByMonth(event.getMonth()).isPresent())&&(eventRepository.findByYear(event.getYear()).isPresent())){
            return null;
        } else{
            eventRepository.save(event);
            return event;
        }
    }

       public Event findEventById(Long id){
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()){
            return event.get();
        }else{
            return null;
        }
    }
}
