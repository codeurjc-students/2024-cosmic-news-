package es.codeurjc.cosmic_news.controller;

import java.io.IOException;
import java.sql.Time;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.service.QuizzService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class QuizzesController {

    @Autowired
    QuizzService quizzService;

    @GetMapping("/quizzes")
    public String getQuizzes(Model model) {
        model.addAttribute("quizzes", quizzService.getAllQuizzes());
        return "quizzes";
    }
     
    @GetMapping("/quizz/new")
    public String getQuizzForm(Model model) {

        model.addAttribute("edit", false);

        return "quizz_form";
    }

    @PostMapping("/quizz/new")
    public String newQuizz(Model model, MultipartFile photoField, HttpServletRequest request) throws IOException{
        quizzService.saveQuizz(request, photoField);
        return "redirect:/quizzes";
    }

    @PostMapping("/quizz/delete/{id}")
	public String removeQuizz(@PathVariable Long id, Model model) {
		Quizz removedQuizz = quizzService.removeQuizz(id);

		if (removedQuizz == null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "El quizz no se ha podido borrar");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
		} else {
			return "redirect:/quizzes";
		}
	}

    @GetMapping("/quizz/{id}")
    public String getQuizz(@PathVariable Long id, Model model) {
        Quizz quizz = quizzService.findQuizzById(id);
        if (quizz == null){
            model.addAttribute("title", "Error");
            model.addAttribute("message", "No se ha encontrado el quizz");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        }else{
            List<Question> questions = quizz.getQuestions();
            model.addAttribute("quizz", quizz);
            model.addAttribute("questions", questions);
            return "quizz";
        }
    }

    @PostMapping("/quizz/submit/{id}")
    public String submitQuizz(@PathVariable Long id, Model model, HttpServletRequest request) {
        Quizz quizz = quizzService.submitQuizz(id, request);
        if (quizz == null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "El quizz no se ha podido procesar");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        } else {
            model.addAttribute("completed", quizz.getScore() == quizz.getQuestions().size());
            model.addAttribute("quizz", quizz);
            return "result";
        }
    }

    @GetMapping("/quizz/review/{id}")
    public String reviewQuizz(@PathVariable Long id, Model model) {
        Quizz quizz = quizzService.findQuizzById(id);
        if (quizz == null) {
            model.addAttribute("title", "Error");
            model.addAttribute("message", "El quizz no se ha encontrado");
            model.addAttribute("back", "javascript:history.back()");
            return "message";
        } else {
            model.addAttribute("quizz", quizz);
            //quizzService.unselect(quizz);
            return "review";
        }
    }
    
    
}
