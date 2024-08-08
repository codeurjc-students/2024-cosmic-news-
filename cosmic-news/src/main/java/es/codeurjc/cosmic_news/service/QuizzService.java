package es.codeurjc.cosmic_news.service;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.cosmic_news.model.Badge;
import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.QuestionRepository;
import es.codeurjc.cosmic_news.repository.QuizzRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class QuizzService {

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private UserRepository userRepository;
    
    public List<Quizz> getAllQuizzes(){
        return quizzRepository.findAll();
    }
    
    public Quizz saveQuizz(HttpServletRequest request, MultipartFile photoField) throws IOException{
        
        if (quizzRepository.findByName(request.getParameter("name")).isPresent()){
            return null;
        } else{
            Quizz quizz = new Quizz();
            quizz.setName(request.getParameter("name"));
            quizz.setDifficulty(request.getParameter("difficulty"));

            if(!photoField.isEmpty()){
                quizz.setPhoto(BlobProxy.generateProxy(photoField.getInputStream(), photoField.getSize()));
                quizz.setImage(true);
            }

            String numQuestionsStr = request.getParameter("numQuestions");
            if (numQuestionsStr != null && !numQuestionsStr.isEmpty()) {
                int numQuestions = Integer.parseInt(numQuestionsStr);
                List<Question> questions = new ArrayList<>();
                for (int i = 0; i < numQuestions; i++){
                    String question = request.getParameter("questions[" + i + "][question]");
                    String option1 = request.getParameter("questions[" + i + "][option1]");
                    String option2 = request.getParameter("questions[" + i + "][option2]");
                    String option3 = request.getParameter("questions[" + i + "][option3]");
                    String option4 = request.getParameter("questions[" + i + "][option4]");
                    String answer = request.getParameter("questions[" + i + "][answer]");

                    Question q = new Question(question, option1, option2, option3, option4, answer, i+1, quizz);

                    switch (answer){
                        case "option1":
                        answer = option1;
                        q.setCorrect1(true);
                        q.setCorrect2(false);
                        q.setCorrect3(false);
                        q.setCorrect4(false);
                        break;
                        case "option2":
                        answer = option2;
                        q.setCorrect2(true);
                        q.setCorrect1(false);
                        q.setCorrect3(false);
                        q.setCorrect4(false);
                        break;
                        case "option3":
                        answer = option3;
                        q.setCorrect3(true);
                        q.setCorrect1(false);
                        q.setCorrect2(false);
                        q.setCorrect4(false);
                        break;
                        case "option4":
                        answer = option4;
                        q.setCorrect4(true);
                        q.setCorrect1(false);
                        q.setCorrect2(false);
                        q.setCorrect3(false);                   
                        break;
                    }

                    q.setAnswer(answer);
                    questions.add(q);
                }
                quizz.setQuestions(questions);
            }
            quizzRepository.save(quizz);
            return quizz;
        }
    }

    public Quizz removeQuizz(Long id) {
		Optional<Quizz> quizz = quizzRepository.findById(id);

		if (quizz.isPresent()) {
			quizzRepository.deleteById(id);

			return quizz.get();
		} else {
			return null;
		}
	}

    public Quizz submitQuizz(Long id, HttpServletRequest request) {
        Optional<Quizz> quizzOp = quizzRepository.findById(id);
        if (!quizzOp.isPresent()) {
            return null;
        } else {
            Quizz quizz = quizzOp.get();
            List<Question> questions = quizz.getQuestions();
            int correctAnswers = 0;
    
            for (Question question : questions) {
                String selectedOption = request.getParameter("question-" + question.getId());
                if (selectedOption != null){
                    if (selectedOption.equals(question.getOption1())){
                        question.setSelect1(true);
                        question.setSelect2(false);
                        question.setSelect3(false);
                        question.setSelect4(false);
                    }
                    if (selectedOption.equals(question.getOption2())){
                        question.setSelect2(true);
                        question.setSelect1(false);
                        question.setSelect3(false);
                        question.setSelect4(false);
                    }
                    if (selectedOption.equals(question.getOption3())){
                        question.setSelect3(true);
                        question.setSelect1(false);
                        question.setSelect2(false);
                        question.setSelect4(false);
                    }
                    if (selectedOption.equals(question.getOption4())){
                        question.setSelect4(true);
                        question.setSelect1(false);
                        question.setSelect2(false);
                        question.setSelect3(false);
                    }
                    if (selectedOption.equals(question.getAnswer())) {
                        correctAnswers++;
                    }
                }
            }

            quizz.setScore(correctAnswers);
            quizzRepository.save(quizz);
    
            return quizz;
        }
    }

    public void giveBadge(Quizz quizz, String mail){
        Optional<User> userOp = userRepository.findByMail(mail);
        if (userOp.isPresent()){
            if (quizz.isImage()){
                User user = userOp.get();
                if (!user.hasBadge(quizz.getName())){
                    int length = user.getBadges().size();
                    user.addBadge(new Badge(quizz.getPhoto(),quizz.getName(), length));
                    userRepository.save(user);
                }
            }
            
        }
    }

    public Quizz findQuizzById(Long id){
        Optional<Quizz> quizz = quizzRepository.findById(id);
        if (quizz.isPresent()){
            return quizz.get();
        }else{
            return null;
        }
    }

    public void unselect(Quizz quizz){
        List<Question> questions = quizz.getQuestions();
            for (Question question : questions) {
                question.setSelect1(false);
                question.setSelect2(false);
                question.setSelect3(false);
                question.setSelect4(false);
            }
        quizzRepository.save(quizz);
    }
}
