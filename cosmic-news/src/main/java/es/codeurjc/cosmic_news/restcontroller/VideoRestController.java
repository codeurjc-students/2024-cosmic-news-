package es.codeurjc.cosmic_news.restcontroller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import es.codeurjc.cosmic_news.DTO.VideoDTO;
import es.codeurjc.cosmic_news.model.Video;
import es.codeurjc.cosmic_news.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/videos")
public class VideoRestController {
    @Autowired
    private VideoService videoService;
     @Operation(summary = "Get paged videos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Videos found", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = VideoDTO.class)) }),
        @ApiResponse(responseCode = "204", description = "Videos not found, probably high page number supplied", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<VideoDTO>> getVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Video> videos = videoService.findAll(PageRequest.of(page, size));
        List<VideoDTO> videosDTO = new ArrayList<>();

        for (Video video : videos) {
            videosDTO.add(new VideoDTO(video));
        }

        if (videosDTO.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(videosDTO);
    }

        @Operation(summary = "Get a video by its id")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Found the video",
	 content = {@Content(
	 mediaType = "application/json",
	 schema = @Schema(implementation=Video.class)
	 )}
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Video not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideo(@PathVariable long id){ 
        Video video = videoService.findVideoById(id);
        if (video != null) return ResponseEntity.status(HttpStatus.OK).body(new VideoDTO(video));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a video by its id. You need to be an administrator")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Video deleted",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "401",
	 description = "You are not authorized",
	 content = @Content
	 ),
	 @ApiResponse(
	 responseCode = "404",
	 description = "Video not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable long id, Principal principal){
        if(principal !=null){
			Video video = videoService.findVideoById(id);
			if (video != null){
				videoService.deleteVideo(video.getId());
				return ResponseEntity.status(HttpStatus.OK).build();
			}else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Post a new video")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Video posted correctly",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=VideoDTO.class)
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
	public ResponseEntity<Video> createVideo(@RequestBody VideoDTO videoDTO) {
        Video video = videoDTO.toVideo();
		videoService.saveVideo(video);
		return new ResponseEntity<>(video, HttpStatus.OK);
	}

    @Operation(summary = "Update a video fields by ID. You need to be an administrator.")
	@ApiResponses(value = {
	 @ApiResponse(
	 responseCode = "200",
	 description = "Video updated correctly. You can update only the fields you want",
	 content = {@Content(
		mediaType = "application/json",
		schema = @Schema(implementation=VideoDTO.class)
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
	 description = "Video not found, probably invalid id supplied",
	 content = @Content
	 )
	})
    @PutMapping("/{id}")
	public ResponseEntity<Video> updateVideo(@PathVariable long id, @RequestBody VideoDTO videoDTO, Principal principal) throws SQLException {
        Video video = videoService.findVideoById(id);
			if (video != null) {
                updateVideo(video, videoDTO);
				return new ResponseEntity<>(video, HttpStatus.OK);
			} else	{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
	}
    public void updateVideo(Video video, VideoDTO newVideo){
		if (newVideo.getTitle()!=null) video.setTitle(newVideo.getTitle());
        if (newVideo.getDuration()!=null) video.setDuration(newVideo.getDuration());
        if (newVideo.getDescription()!=null) video.setDescription(newVideo.getDescription());
		if (newVideo.getVideoUrl()!=null) video.setVideoUrl(newVideo.getVideoUrl());
		videoService.saveVideo(video);
	}
}
