package es.codeurjc.cosmic_news.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.model.Video;
import es.codeurjc.cosmic_news.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class VideosController {
    @Autowired
    VideoService videoService;
    
    @GetMapping("/videos")
    public String getVideos(Model model) {
        model.addAttribute("videos", videoService.getAllVideos());
        return "videos";
    }
     
    @GetMapping("/video/new")
    public String getVideosForm(Model model) {

        model.addAttribute("edit", false);

        return "video_form";
    }

    @PostMapping("/video/new")
    public String createVideo(Model model, Video video, MultipartFile photoField, HttpServletRequest request) throws IOException{

        Video createdVideo = videoService.saveVideo(video);
        if (createdVideo== null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al crear el vídeo");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        } else {
            return "redirect:/videos";
        }
    }


    @GetMapping("/video/{id}")
    public String showVideo(Model model, HttpServletRequest request, @PathVariable Long id) {

        Video video = videoService.findVideoById(id);
        if (video != null){
            model.addAttribute("video", video);
            return "/video_info";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Vídeo no encontrado");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }

    @GetMapping("/video/{id}/delete")
    public String deleteVideo(Model model, HttpServletRequest request, @PathVariable Long id) {

        boolean deleted = videoService.deleteVideo(id);

        if(deleted){
            return "redirect:/videos";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al eliminar el vídeo");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }

    }

    @GetMapping("/video/{id}/edit")
    public String showVideoFormEdit(@PathVariable Long id, Model model, HttpServletRequest request) {
        Video video = videoService.findVideoById(id);
        if (video != null){
        model.addAttribute("video", video);
        model.addAttribute("edit", true);
        return "video_form";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Vídeo no encontrado");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }

    @PostMapping("/video/{id}/edit")
    public String editVideo(@PathVariable Long id, HttpServletRequest request, Model model) throws IOException {
        Video video = videoService.findVideoById(id);
        if (video != null){
            videoService.updateVideo(video,request);
            return "redirect:/video/{id}";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Vídeo no encontrado");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }
}
