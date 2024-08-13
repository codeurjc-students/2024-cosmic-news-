package es.codeurjc.cosmic_news.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.PictureService;
import es.codeurjc.cosmic_news.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PicturesController {

    @Autowired
    PictureService pictureService;

    @Autowired
    UserService userService;

    @GetMapping("/pictures")
    public String getPictures(Model model, Pageable page, HttpServletRequest request) {
        model.addAttribute("admin", request.isUserInRole("ADMIN"));
        return "pictures";
    }

    @GetMapping("/pictures/load")
    public String loadPictures(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size, @RequestParam("filter") String filter) {
        
        Page<Picture> pictures = null;
        if (filter.equals("likes")){
            pictures = pictureService.findAllByLikes(PageRequest.of(pageNumber, size));
        }else if(filter.equals("date")){
            pictures = pictureService.findAllByDate(PageRequest.of(pageNumber, size));
        }else{
            pictures = pictureService.findAll(PageRequest.of(pageNumber, size));
        }
        model.addAttribute("pictures", pictures);
        model.addAttribute("hasMore", pictures.hasNext());
        return "picture_cards";
    }

    @GetMapping("/picturesUser/load")
    public String loadPicturesUsers(HttpServletRequest request, Model model, @RequestParam("page") int pageNumber,
            @RequestParam("size") int size) {

        if (request.getUserPrincipal() != null) {
            User user = userService.findUserByMail(request.getUserPrincipal().getName());
            if (user != null){
                Page<Picture> pictures = pictureService.findAllByUserId(user.getId(),pageNumber,size);
                model.addAttribute("pictures", pictures);
                model.addAttribute("hasMore", pictures.hasNext());
                return "picture_cards";
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
     
    @GetMapping("/picture/new")
    public String getPicturesForm(Model model) {

        model.addAttribute("edit", false);

        return "picture_form";
    }

    @PostMapping("/picture/new")
    public String createPicture(Model model, Picture picture, MultipartFile photoField, HttpServletRequest request) throws IOException{
        if(!photoField.isEmpty()){
            picture.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
            picture.setImage(true);
            }

        Picture createdPicture = pictureService.savePicture(picture);
        if (createdPicture== null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al crear la foto");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        } else {
            return "redirect:/pictures";
        }
    }


    @GetMapping("/pictures/{id}")
    public String showPicture(Model model, HttpServletRequest request, @PathVariable Long id) {

        Picture picture = pictureService.findPictureById(id);
        if (picture != null){
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("picture", picture);
            return "/picture_info";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Foto no encontrada");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }


    @GetMapping("/picture/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Picture picture = pictureService.findPictureById(id);
        if (picture != null && picture.getPhoto() != null) {

            Resource file = new InputStreamResource(picture.getPhoto().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(picture.getPhoto().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/picture/{id}/delete")
    public String deletePicture(Model model, HttpServletRequest request, @PathVariable Long id) {

        boolean deleted = pictureService.deletePicture(id);

        if(deleted){
            return "redirect:/pictures";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Error al eliminar la foto");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }

    }

    @GetMapping("/picture/{id}/edit")
    public String showPictureFormEdit(@PathVariable Long id, Model model, HttpServletRequest request) {
        Picture picture = pictureService.findPictureById(id);
        if (picture != null){
        model.addAttribute("picture", picture);
        model.addAttribute("edit", true);
        return "picture_form";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Foto no encontrada");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }

    @PostMapping("/picture/{id}/edit")
    public String editPicture(@PathVariable Long id, HttpServletRequest request, Model model, @RequestParam("photoField") MultipartFile photoField) throws IOException {
        Picture picture = pictureService.findPictureById(id);
        if (picture != null){
            if (!photoField.isEmpty()){
                picture.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
            }
            pictureService.updatePicture(picture,request);
            return "redirect:/pictures/{id}";
        }else{
            model.addAttribute("title", "Error");
            model.addAttribute("message", "Foto no encontrada");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }
    }

    @GetMapping("/picture/{id}/like")
    public String likePicture(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            User user = userService.findUserByMail(request.getUserPrincipal().getName());
            Picture picture = pictureService.findPictureById(id);
            if (user != null && picture != null){
                pictureService.like(picture,user);
                return "redirect:/pictures/{id}";
            }else{
                model.addAttribute("title", "Error");
                model.addAttribute("message", "Usuario o foto no encontrados.");
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
