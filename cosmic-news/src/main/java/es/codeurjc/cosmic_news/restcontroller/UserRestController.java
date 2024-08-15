package es.codeurjc.cosmic_news.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.cosmic_news.DTO.UserDTO;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;
    
    @Operation(summary = "Get a user by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the user",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=User.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "User not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id){ 
        User user = userService.findUserById(id);
        if (user != null) return ResponseEntity.status(HttpStatus.OK).body(user);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a user by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "User not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, Principal principal){ //Only for admin and owner
        if(principal !=null){
			User user = userService.findUserById(id);
			if (user != null){
				userService.deleteUser(user.getMail());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new user")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=UserDTO.class)
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
	public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
		String messageForm = userService.checkForm(userDTO.getMail(), userDTO.getNick());
		if (messageForm.equals("")) {
            User user = userDTO.toUser();
			userService.saveUser(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}else{
			HttpHeaders headers = new HttpHeaders();
			headers.add("Error-Message", messageForm);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
		}
	}

    @Operation(summary = "Update a user fields by ID. You need to be an administrator or the own user.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "User updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=UserDTO.class)
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
	 description = "User not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO, Principal principal) throws SQLException {
		if(principal !=null){
            User user = userService.findUserById(id);
			if (user != null) {
				String messageForm = userService.checkForm(userDTO.getMail(), userDTO.getNick());
				if (messageForm.equals("")) {
					if (userDTO.getNick() == null){
						if (userDTO.getMail()==null){
							updateUser(user, userDTO);
							return new ResponseEntity<>(user, HttpStatus.OK);
						}else if(!userDTO.getMail().equals(user.getMail())){
							updateUser(user, userDTO);
							return new ResponseEntity<>(user, HttpStatus.OK);
						}else{
							HttpHeaders headers = new HttpHeaders();
							headers.add("Error-Message", messageForm);
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
						}					
					}else if (!user.getNick().equals(userDTO.getNick())){
						if (userDTO.getMail()==null){
							updateUser(user, userDTO);
							return new ResponseEntity<>(user, HttpStatus.OK);
						}else if(!userDTO.getMail().equals(user.getMail())){
							updateUser(user, userDTO);
							return new ResponseEntity<>(user, HttpStatus.OK);
						}else{
							HttpHeaders headers = new HttpHeaders();
							headers.add("Error-Message", messageForm);
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
						}
					}else{
						HttpHeaders headers = new HttpHeaders();
						headers.add("Error-Message", messageForm);
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
					}
				}else{
					HttpHeaders headers = new HttpHeaders();
					headers.add("Error-Message", messageForm);
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
				}
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Post a new photo of a user by id")
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
	public ResponseEntity<Object> uploadPhotoUser(@PathVariable long id, @RequestParam MultipartFile imageFile, Principal principal)
			throws IOException {
		if(principal !=null){
            User user = userService.findUserById(id);
			if (user != null) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				user.setImage(true);
				user.setPhoto(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				userService.saveUserRest(user);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
 
	@Operation(summary = "Get the photo a user by id")
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
	 description = "That user does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadPhoto(@PathVariable long id) throws SQLException {
        User user = userService.findUserById(id);
		if (user != null) {
		
			if (user.getPhoto() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(user.getPhoto().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(user.getPhoto().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the photo of a user by id. You need to be an admin or the own user")
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
            User user = userService.findUserById(id);
			if (user != null) {
				user.setPhoto(null);
				user.setImage(false);

				userService.saveUserRest(user);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}


	@Operation(summary = "Get the news of a user by id. You need to be an admin or the own employer")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the news list of the user",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),	
	 @ApiResponse(
	 responseCode = "404",
	 description = "Data entered incorrectly, probably invalid id supplied",
	 content = @Content
	 ),	 
	})
	@GetMapping("/{id}/news")
	public ResponseEntity<List<News>> getUserNews(@PathVariable long id, Principal principal){ 
    	if(principal !=null){
			User user = userService.findUserById(id);
			if (user != null) {

				List<News> news = user.getNews().stream()
					.map(n -> new News(n.getTitle(), n.getSubtitle(), n.getAuthor(), n.getTopic(), n.getBodyText(), n.getReadingTime(), n.getDate()))
					.collect(Collectors.toList());
			
				return ResponseEntity.status(HttpStatus.OK).body(news);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    public void updateUser(User user, UserDTO newUser){
		if (newUser.getName()!=null) user.setName(newUser.getName());
        if (newUser.getNick()!=null) user.setNick(newUser.getNick());
        if (newUser.getDescription()!=null) user.setDescription(newUser.getDescription());
		if (newUser.getSurname()!=null) user.setSurname(newUser.getSurname());
		userService.saveUserRest(user);
	}

}
