package es.codeurjc.cosmic_news.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.EventService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;






@Controller
public class CalendarController {

    @Autowired
    EventService eventService;
    
    @GetMapping("/calendar")
    public String getCalendar() {
        return "calendar";
    }
    
    @GetMapping("/event/new")
    public String newEvent() {
        return "event_form";
    }

    @PostMapping("/event/new")
    public String postEvent(Model model, Event event) {
        Event newEvent = eventService.saveEvent(event);

        if (newEvent == null){
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al crear el evento");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }else{
            return "redirect:/calendar";
        }
        
    }

    @GetMapping("/event/edit/{id}")
    public String showFormEdit(@PathVariable Long id, Model model, HttpServletRequest request) {
        Event event = eventService.findEventById(id);
        if (event != null){
        model.addAttribute("event", event);
        return "event_form";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Evento no encontrado");
            model.addAttribute("back", "/");
            return "message";
        }
    }
    

    @GetMapping("/events")
    @ResponseBody
    public List<Event> getEvents() {
        return eventService.getAllEvents();
    }
    
}
