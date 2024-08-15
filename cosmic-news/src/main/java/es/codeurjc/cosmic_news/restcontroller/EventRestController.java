package es.codeurjc.cosmic_news.restcontroller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.cosmic_news.DTO.EventDTO;
import es.codeurjc.cosmic_news.DTO.PictureDTO;
import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/events")
public class EventRestController {
    @Autowired
    private EventService eventService;

    @Operation(summary = "Get events.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class)) }),
    })
    @GetMapping()
	public Collection<Event> getEvents() {
		return eventService.getAllEvents();
	}

        @Operation(summary = "Get an event by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the event",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=Event.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Event not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable long id){ 
        Event event = eventService.findEventById(id);
        if (event != null) return ResponseEntity.status(HttpStatus.OK).body(new EventDTO(event));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete an event by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Event deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Event not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id, Principal principal){
        if(principal !=null){
			Event event = eventService.findEventById(id);
			if (event != null){
				eventService.deleteEvent(event.getId());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new event")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Event posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=EventDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 )
	})
    @PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = eventDTO.toEvent();
		eventService.saveEvent(event);
		return new ResponseEntity<>(event, HttpStatus.OK);
	}

    @Operation(summary = "Update a event fields by ID. You need to be an administrator.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Event updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=PictureDTO.class)
		)}
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly.",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Event not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<Event> updateEvent(@PathVariable long id, @RequestBody EventDTO eventDTO, Principal principal) throws SQLException {
        Event event = eventService.findEventById(id);
			if (event != null) {
                updateEvent(event, eventDTO);
				return new ResponseEntity<>(event, HttpStatus.OK);
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
	}

    public void updateEvent(Event event, EventDTO newEvent){
		if (newEvent.getDate()!=null) event.setDate(newEvent.getDate());
        if (newEvent.getIcon()!=null) event.setIcon(newEvent.getIcon());
        if (newEvent.getDescription()!=null) event.setDescription(newEvent.getDescription());
		eventService.saveEvent(event);
	}
    
}
