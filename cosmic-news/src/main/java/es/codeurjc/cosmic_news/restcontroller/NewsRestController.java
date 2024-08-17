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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.cosmic_news.DTO.NewsDTO;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.NewsService;
import es.codeurjc.cosmic_news.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/news")
public class NewsRestController {
    @Autowired
    NewsService newsService;

	@Autowired
    UserService userService;
    
     @Operation(summary = "Get paged news.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "News found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = NewsDTO.class)) }),
        @ApiResponse(responseCode = "204", description = "News not found, probably high page number supplied", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NewsDTO>> getNewsPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "date") String filter) {

		Page<News> newsPage = null;
		if (filter.equals("likes")){
            newsPage = newsService.findAllByLikes(PageRequest.of(page, size));
        }else if(filter.equals("date")){
            newsPage = newsService.findAllByDate(PageRequest.of(page, size));
        }else if(filter.equals("time")){
            newsPage = newsService.findAllByTime(PageRequest.of(page, size));
        }else{
            newsPage = newsService.findAll(PageRequest.of(page, size));
        }
        List<NewsDTO> newsList = new ArrayList<>();

        for (News news : newsPage) {
            newsList.add(new NewsDTO(news));
        }

        if (newsList.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(newsList);
    }

    @Operation(summary = "Get a news by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the news",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=News.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "News not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNews(@PathVariable long id){ 
        News news = newsService.findNewsById(id);
        if (news != null) return ResponseEntity.status(HttpStatus.OK).body(new NewsDTO(news));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a news by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "News deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "News not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable long id, Principal principal){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
			News news = newsService.findNewsById(id);
			if (news != null){
				newsService.deleteNews(news.getId());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new news")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "News posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=NewsDTO.class)
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
		)
	})
    @PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<News> createNews(@RequestBody NewsDTO newsDTO, Principal principal) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
			News news = newsDTO.toNews();
			newsService.saveNews(news);
			return new ResponseEntity<>(news, HttpStatus.OK);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Update a news fields by ID. You need to be an administrator.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "News updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=NewsDTO.class)
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
	 description = "News not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<News> updateNews(@PathVariable long id, @RequestBody NewsDTO newsDTO, Principal principal) throws SQLException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
			News news = newsService.findNewsById(id);
			if (news != null) {
				updateNews(news, newsDTO);
				return new ResponseEntity<>(news, HttpStatus.OK);
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

    @Operation(summary = "Post a new news of a picture by id")
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
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
            News news = newsService.findNewsById(id);
			if (news != null) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

				news.setImage(true);
				news.setPhoto(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				newsService.saveNewsRest(news);

				return ResponseEntity.created(location).build();
			}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
 
	@Operation(summary = "Get the photo a news by id")
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
	 description = "That news does not have a photo",
	 content = @Content
	 )
	})
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadPhoto(@PathVariable long id) throws SQLException {
        News news = newsService.findNewsById(id);
		if (news != null) {
		
			if (news.getPhoto() != null) {

				org.springframework.core.io.Resource file = new InputStreamResource(news.getPhoto().getBinaryStream());

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
						.contentLength(news.getPhoto().length()).body(file);

			} else {
				return ResponseEntity.notFound().build();
			}
		}else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Delete the photo of a news by id. You need to be an admin")
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
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(principal !=null && request.isUserInRole("ADMIN")){
            News news = newsService.findNewsById(id);
			if (news != null) {
				news.setPhoto(null);
				news.setImage(false);

				newsService.saveNewsRest(news);

				return ResponseEntity.noContent().build();
			}else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@Operation(summary = "Give a like to the news by id. If you already gave like to this news, this will be a unlike")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Like/Unlike given",
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
	@PostMapping("/{id}/like")
	public ResponseEntity<News> likeNews(@PathVariable long id, Principal principal) {
		if (principal != null) {
            User user = userService.findUserByMail(principal.getName());
            News news = newsService.findNewsById(id);
            if (user != null && news != null){
                newsService.like(news,user);
				return new ResponseEntity<>(news, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
	}
	
    public void updateNews(News news, NewsDTO newsDTO){
		if (newsDTO.getTitle()!=null) news.setTitle(newsDTO.getTitle());
        if (newsDTO.getAuthor()!=null) news.setAuthor(newsDTO.getAuthor());
        if (newsDTO.getSubtitle()!=null) news.setSubtitle(newsDTO.getSubtitle());
        if (newsDTO.getTopic()!=null) news.setTopic(newsDTO.getTopic());
        if (newsDTO.getReadingTime()!=null) news.setReadingTime(Integer.parseInt(newsDTO.getReadingTime()));
        if (newsDTO.getBodyText()!=null) news.setBodyText(newsDTO.getBodyText());
		if (newsDTO.getDate()!=null) news.setDate(newsDTO.getDate());
		newsService.saveNewsRest(news);
	}
}
