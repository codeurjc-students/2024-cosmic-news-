package es.codeurjc.cosmic_news.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.NewsService;
import es.codeurjc.cosmic_news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @GetMapping("/news/load")
    public String loadNews(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size, @RequestParam("filter") String filter) {
        
        Page<News> news = null;
        if (filter.equals("likes")){
            news = newsService.findAllByLikes(PageRequest.of(pageNumber, size));
        }else if(filter.equals("date")){
            news = newsService.findAllByDate(PageRequest.of(pageNumber, size));
        }else if(filter.equals("time")){
            news = newsService.findAllByTime(PageRequest.of(pageNumber, size));
        }else{
            news = newsService.findAll(PageRequest.of(pageNumber, size));
        }

        model.addAttribute("news", news);
        model.addAttribute("hasMore", news.hasNext());
        return "news_cards";
    }

    @GetMapping("/newsUser/load")
    public String loadNewsUsers(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size) {

        if (request.getUserPrincipal() != null) {
            User user = userService.findUserByMail(request.getUserPrincipal().getName());
            if (user != null){
                Page<News> news = newsService.findAllByUserId(user.getId(),pageNumber,size);
                model.addAttribute("news", news);
                model.addAttribute("hasMore", news.hasNext());
                return "news_cards";
            }else{
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Usuario no encontrado");
                model.addAttribute("back", "javascript:history.back()");
                return "message";
            }
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Usuario no encontrado");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }

    }
     
    @GetMapping("/news/new")
    public String getNewsForm(Model model) {

        model.addAttribute("edit", false);

        return "news_form";
    }

    @PostMapping("/news/new")
    public String createNews(Model model, News news, MultipartFile photoField, HttpServletRequest request) throws IOException{
        if(!photoField.isEmpty()){
            news.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
            news.setImage(true);
            }

        News createdNews = newsService.saveNews(news);
        if (createdNews== null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al crear la noticia");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/new/{id}")
    public String showNews(Model model, HttpServletRequest request, @PathVariable Long id) {

        News news = newsService.findNewsById(id);
        if (news != null){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("news", news);
            return "/news_info";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Noticia no encontrada");
            model.addAttribute("back", "/");
            return "message";
        }
    }


    @GetMapping("/news/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        News news = newsService.findNewsById(id);
        if (news != null && news.getPhoto() != null) {

            Resource file = new InputStreamResource(news.getPhoto().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(news.getPhoto().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/news/{id}/delete")
    public String deleteNews(Model model, HttpServletRequest request, @PathVariable Long id) {

        boolean deleted = newsService.deleteNews(id);

        if(deleted){
            return "redirect:/";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al eliminar la noticia");
            model.addAttribute("back", "/");
            return "message";
        }

    }

    @GetMapping("/news/{id}/edit")
    public String showNewsFormEdit(@PathVariable Long id, Model model, HttpServletRequest request) {
        News news = newsService.findNewsById(id);
        if (news != null){
        model.addAttribute("news", news);
        model.addAttribute("edit", true);
        return "news_form";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Noticia no encontrada");
            model.addAttribute("back", "/");
            return "message";
        }
    }

    @PostMapping("/news/{id}/edit")
    public String editNews(@PathVariable Long id, HttpServletRequest request, Model model, @RequestParam("photoField") MultipartFile photoField) throws IOException {

        News news = newsService.findNewsById(id);
        if (news != null){
            if (!photoField.isEmpty()){
                news.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
            }
            newsService.updateNews(news,request);
            return "redirect:/new/{id}";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Noticia no encontrada");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }

    @GetMapping("/news/{id}/like")
    public String likeNews(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            User user = userService.findUserByMail(request.getUserPrincipal().getName());
            News news = newsService.findNewsById(id);
            if (user != null && news != null){
                newsService.like(news,user);
                return "redirect:/new/{id}";
            }else{
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Usuario o noticia no encontrados.");
                model.addAttribute("back", "javascript:history.back()");
                return "message";
            }
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "¡Regístrate para poder dar likes a las fotos!");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }
}
