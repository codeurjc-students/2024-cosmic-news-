package es.codeurjc.cosmic_news.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.QuizzRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase(){
        initUsers();
        initQuizzes();
    }

    private void initUsers(){
        User user1 = new User(
            "Juan",
            "Saturno",
            "Usuario inicializado",
            "JuanitoSaturno42",
            "juanSaturno@gmail.com",
            passwordEncoder.encode("juan"),
            "USER");
        
        User user2 = new User(
            "Pepe",
            "Jupiter",
            "Usuario inicializado",
            "PepitoInJupiter",
            "a",
            passwordEncoder.encode("a"),
            "USER");
        
        User user3 = new User(
            "Pedro",
            "Admin",
            "Admin inicializado",
            "GodPedro",
            "xd",
            passwordEncoder.encode("xd"),
            "USER","ADMIN");
        
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        
    }

    private void initQuizzes(){
        //Initialize quizz1
        Quizz quizz1 = new Quizz();
        quizz1.setName("Bienvenida");
        quizz1.setDifficulty("Fácil");

        Blob photo = null;
        photo = quizz1.photoToBlob("static/images/logo.png");
        quizz1.setPhoto(photo);
        quizz1.setImage(photo != null);

        List<Question> questions = new ArrayList<Question>();
        Question question1 = new Question();
        question1.setQuestion("¿Cómo se llama la página web?");
        question1.setOption1("Astro News");
        question1.setOption2("Cosmic News");
        question1.setOption3("Astronomy For All");
        question1.setOption4("Hidden World");
        question1.setAnswer("Cosmic News");
        question1.setNum(1);
        question1.setQuizz(quizz1);

        question1.setCorrect1(false);
        question1.setCorrect2(true);
        question1.setCorrect3(false);
        question1.setCorrect4(false);

        questions.add(question1);

        Question question2 = new Question();
        question2.setQuestion("¿Si tienes alguna duda dónde debes acudir?");
        question2.setOption1("Foro");
        question2.setOption2("Perfil");
        question2.setOption3("Fotos");
        question2.setOption4("Calendario");
        question2.setAnswer("Foro");
        question2.setNum(2);
        question2.setQuizz(quizz1);

        question2.setCorrect1(true);
        question2.setCorrect2(false);
        question2.setCorrect3(false);
        question2.setCorrect4(false);

        questions.add(question2);

        Question question3 = new Question();
        question3.setQuestion("¿Qué puedes encontrar en la aplicación?");
        question3.setOption1("Noticias");
        question3.setOption2("Fotos");
        question3.setOption3("Vídeos");
        question3.setOption4("Todas son correctas");
        question3.setAnswer("Todas son correctas");
        question3.setNum(3);
        question3.setQuizz(quizz1);

        question3.setCorrect1(false);
        question3.setCorrect2(false);
        question3.setCorrect3(false);
        question3.setCorrect4(true);

        questions.add(question3);

        Question question4 = new Question();
        question4.setQuestion("¿Dónde puedes encontrar la fecha de eventos astronómicos?");
        question4.setOption1("Perfil");
        question4.setOption2("Foro");
        question4.setOption3("Calendario");
        question4.setOption4("Quizzes");
        question4.setAnswer("Calendario");
        question4.setNum(4);
        question4.setQuizz(quizz1);

        question4.setCorrect1(false);
        question4.setCorrect2(false);
        question4.setCorrect3(true);
        question4.setCorrect4(false);

        questions.add(question4);

        Question question5 = new Question();
        question5.setQuestion("¿Puedes dar like y guardarte fotos y noticias?");
        question5.setOption1("Sí");
        question5.setOption2("No");
        question5.setOption3("Sólo fotos");
        question5.setOption4("Sólo noticias");
        question5.setAnswer("Sí");
        question5.setNum(5);
        question5.setQuizz(quizz1);

        question5.setCorrect1(true);
        question5.setCorrect2(false);
        question5.setCorrect3(false);
        question5.setCorrect4(false);

        questions.add(question5);

        quizz1.setQuestions(questions);
        quizzRepository.save(quizz1);
    }

}
