package es.codeurjc.cosmic_news.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.repository.NewsRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class NewsService {

    @Autowired
    NewsRepository newsRepository;
    
    public List<News> getAllNews(){
        return newsRepository.findAll();
    }

    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
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

    public News findNewsById(Long id){
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()){
            return news.get();
        }else{
            return null;
        }
    }
}
