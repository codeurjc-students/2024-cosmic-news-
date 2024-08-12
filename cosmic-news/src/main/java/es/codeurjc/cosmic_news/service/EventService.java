package es.codeurjc.cosmic_news.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.EventRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }
    
    public Event saveEvent(Event event){
        if ((eventRepository.findByDate(event.getDate()).isPresent())){
            return null;
        } else{
            eventRepository.save(event);
            return event;
        }
    }

    public void updateEvent(Event event, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        String dateStr = request.getParameter("date");

        try {
            date = LocalDate.parse(dateStr, formatter);
            event.setDate(date);
        } catch (DateTimeParseException e) {
        }
        event.setIcon(request.getParameter("icon"));
        event.setDescription(request.getParameter("description"));
        eventRepository.save(event);
    }

    public boolean deleteEvent(Long id){
        Optional<Event> event = eventRepository.findById(id);

        if (event.isPresent()){
            for (User user: event.get().getUsers()){
                user.removeEvent(event.get());
                userRepository.save(user);
            }
            eventRepository.deleteById(id);
            return true;
        }else {
            return false;
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
