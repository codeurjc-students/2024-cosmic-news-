package es.codeurjc.cosmic_news.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.cosmic_news.DTO.PictureDTO;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.service.PictureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/pictures")
public class PictureRestController {
    @Autowired
    private PictureService pictureService;

     @Operation(summary = "Get paged pictures.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pictures found", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = PictureDTO.class)) }),
        @ApiResponse(responseCode = "204", description = "Pictures not found, probably high page number supplied", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PictureDTO>> getPictures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Picture> pictures = pictureService.findAll(PageRequest.of(page, size));
        List<PictureDTO> picturesDTO = new ArrayList<>();

        for (Picture picture : pictures) {
            picturesDTO.add(new PictureDTO(picture));
        }

        if (picturesDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(picturesDTO);
    }

        @Operation(summary = "Get a picture by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the picture",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=Picture.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Picture not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<PictureDTO> getPicture(@PathVariable long id){ 
        Picture picture = pictureService.findPictureById(id);
        if (picture != null) return ResponseEntity.status(HttpStatus.OK).body(new PictureDTO(picture));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a picture by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Picture deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Picture not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePicture(@PathVariable long id, Principal principal){
        if(principal !=null){
			Picture picture = pictureService.findPictureById(id);
			if (picture != null){
				pictureService.deletePicture(picture.getId());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new picture")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Picture posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=PictureDTO.class)
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
	public ResponseEntity<Picture> createPicture(@RequestBody PictureDTO pictureDTO) {
        Picture picture = pictureDTO.toPicture();
		pictureService.savePicture(picture);
		return new ResponseEntity<>(picture, HttpStatus.OK);
	}

    @Operation(summary = "Update a picture fields by ID. You need to be an administrator.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Picture updated correctly. You can update only the fields you want",
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
	 description = "Picture not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<Picture> updatePicture(@PathVariable long id, @RequestBody PictureDTO pictureDTO, Principal principal) throws SQLException {
        Picture picture = pictureService.findPictureById(id);
			if (picture != null) {
                updatePicture(picture, pictureDTO);
				return new ResponseEntity<>(picture, HttpStatus.OK);
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
	}

    @Operation(summary = "Post a new photo of a picture by id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "201",
	 description = "Photo posted correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 )
	})
    @PostMapping("/{id}/image")
	public ResponseEntity<Object> uploadPhoto(@PathVariable long id, @RequestParam MultipartFile imageFile, Principal principal)
			throws IOException {
		if(principal !=null){
            Picture picture = pictureService.findPictureById(id);
			if (picture != null) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				picture.setImage(true);
				picture.setPhoto(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				pictureService.savePictureRest(picture);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
 
	@Operation(summary = "Get the photo of a picture by id")
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
	 description = "That picture does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadPhoto(@PathVariable long id) throws SQLException {
        Picture picture = pictureService.findPictureById(id);
		if (picture != null) {
		
			if (picture.getPhoto() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(picture.getPhoto().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(picture.getPhoto().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the photo of a picture by id. You need to be an admin")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Photo deleted correctly",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 )
	})
	@DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deletePhoto(@PathVariable long id, Principal principal) throws IOException {
		if(principal !=null){
            Picture picture = pictureService.findPictureById(id);
			if (picture != null) {
				picture.setPhoto(null);
				picture.setImage(false);

				pictureService.savePictureRest(picture);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    public void updatePicture(Picture picture, PictureDTO newPicture){
		if (newPicture.getTitle()!=null) picture.setTitle(newPicture.getTitle());
        if (newPicture.getAuthor()!=null) picture.setAuthor(newPicture.getAuthor());
        if (newPicture.getLocation()!=null) picture.setLocation(newPicture.getLocation());
        if (newPicture.getDescription()!=null) picture.setDescription(newPicture.getDescription());
		if (newPicture.getDateTaken()!=null) picture.setDateTaken(newPicture.getDateTaken());
		pictureService.savePictureRest(picture);
	}
}
