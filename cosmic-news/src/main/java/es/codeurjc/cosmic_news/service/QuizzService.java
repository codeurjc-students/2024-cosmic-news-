package es.codeurjc.cosmic_news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.repository.QuestionRepository;
import es.codeurjc.cosmic_news.repository.QuizzRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class QuizzService {

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private QuestionRepository questionRepository;
    
    public List<Quizz> getAllQuizzes(){
        return quizzRepository.findAll();
    }
    
    public Quizz saveQuizz(HttpServletRequest request){
        
        if (quizzRepository.findByName(request.getParameter("name")).isPresent()){
            return null;
        } else{
            Quizz quizz = new Quizz();
            quizz.setName(request.getParameter("name"));
            quizz.setDifficulty(request.getParameter("difficulty"));

            String numQuestionsStr = request.getParameter("numQuestions");
            if (numQuestionsStr != null && !numQuestionsStr.isEmpty()) {
                int numQuestions = Integer.parseInt(numQuestionsStr);
                for (int i = 0; i < numQuestions; i++){
                    String question = request.getParameter("questions[" + i + "][question]");
                    String option1 = request.getParameter("questions[" + i + "][option1]");
                    String option2 = request.getParameter("questions[" + i + "][option2]");
                    String option3 = request.getParameter("questions[" + i + "][option3]");
                    String option4 = request.getParameter("questions[" + i + "][option4]");
                    String answer = request.getParameter("questions[" + i + "][answer]");

                    Question q = new Question(question, option1, option2, option3, option4, answer);
                    questionRepository.save(q);

                }
            }

            quizzRepository.save(quizz);
            return quizz;
        }
    }
}
