package es.codeurjc.cosmic_news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.cosmic_news.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByNick(String nick);
    public Optional<User> findByMail(String mail);
}
