package es.codeurjc.cosmic_news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase(){
        initUsers();
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
}
