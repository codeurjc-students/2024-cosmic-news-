package es.codeurjc.cosmic_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.service.QuizzService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class QuizzesController {

    @Autowired
    QuizzService quizzService;

    @GetMapping("/quizzes")
    public String getQuizzes() {
        return "quizzes";
    }
     
    @GetMapping("/quizz/new")
    public String getQuizzForm(Model model) {

        model.addAttribute("edit", false);

        return "quizz_form";
    }

    @PostMapping("/quizz/new")
    public String newQuizz(Model model, MultipartFile photoField, HttpServletRequest request){
        
        //quizzService.saveQuizz(request);
        return "quizzes";
    }
}
