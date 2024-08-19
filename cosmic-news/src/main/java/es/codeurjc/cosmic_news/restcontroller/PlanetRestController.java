package es.codeurjc.cosmic_news.restcontroller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
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
    
}
