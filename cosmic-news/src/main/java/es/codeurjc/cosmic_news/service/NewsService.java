package es.codeurjc.cosmic_news.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.repository.NewsRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class NewsService {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    UserRepository userRepository;
    
    public List<News> getAllNews(){
        return newsRepository.findAll();
    }

    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public Page<News> findAllByLikes(Pageable pageable) {
        return newsRepository.findAllByOrderByLikes(pageable);
    }

    public Page<News> findAllByDate(Pageable pageable) {
        return newsRepository.findAllByOrderByDate(pageable);
    }

    public Page<News> findAllByTime(Pageable pageable) {
        return newsRepository.findAllByOrderByReadingTime(pageable);
    }

    public Page<News> findAllByUserId(Long userId, int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return newsRepository.findAllByUserId(userId, pageable);
    }

    public News saveNews(News news){
        news.setDate(LocalDate.now());
        news.setLikes(0);
        newsRepository.save(news);
        return news;
    }

    public boolean deleteNews(Long id){
        Optional<News> news = newsRepository.findById(id);

        if (news.isPresent()){
            for (User user: news.get().getUsers()){
                user.removeNews(news.get());
                userRepository.save(user);
            }
            newsRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public void updateNews(News news, HttpServletRequest request) {
        news.setTitle(request.getParameter("title"));
        news.setSubtitle(request.getParameter("subtitle"));
        news.setBodyText(request.getParameter("bodyText"));
        news.setTopic(request.getParameter("topic"));
        news.setAuthor(request.getParameter("author"));
        news.setReadingTime(Integer.parseInt(request.getParameter("readingTime")));
        newsRepository.save(news);
    }

    public void like(News news, User user){
        Optional<News> n = findNewsUserById(user, news.getId());
        if (n.isPresent()){
            news.setLikes(news.getLikes()-1);
            user.removeNews(news);
            newsRepository.save(news);
            userRepository.save(user);
        }else{
            news.setLikes(news.getLikes()+1);
            user.addNews(news);
            newsRepository.save(news);
            userRepository.save(user);
        }
    }

    public News findNewsById(Long id){
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()){
            return news.get();
        }else{
            return null;
        }
    }

    public Optional<News> findNewsUserById(User user, long id) {
        return user.getNews().stream()
                .filter(news -> news.getId() == id)
                .findFirst();
    }
}
