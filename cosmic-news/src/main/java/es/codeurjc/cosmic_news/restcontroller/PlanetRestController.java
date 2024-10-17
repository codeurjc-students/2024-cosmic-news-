package es.codeurjc.cosmic_news.restcontroller;

import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.cosmic_news.model.Planet;
import es.codeurjc.cosmic_news.service.PlanetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/planets")
public class PlanetRestController {

    @Autowired
    private PlanetService planetService;

    @Operation(summary = "Get planets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planets found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Planet.class)) }),
    })
    @GetMapping()
	public Collection<Planet> getPlanets() {
		return planetService.getAllPlanets();
	}

    @Operation(summary = "Get the photo of a planet by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the photo",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "400",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "404",
	 description = "That planet does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/photo")
	public ResponseEntity<Object> downloadPhoto(@PathVariable long id) throws SQLException {
        Planet planet = planetService.findPlanetById(id);
		if (planet != null) {
		
			if (planet.getPhoto() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(planet.getPhoto().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(planet.getPhoto().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
    
}
