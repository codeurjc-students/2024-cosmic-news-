package es.codeurjc.cosmic_news.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.cosmic_news.model.Video;
import es.codeurjc.cosmic_news.repository.VideoRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class VideoService {
    @Autowired
    VideoRepository videoRepository;

    public List<Video> getAllVideos(){
        return videoRepository.findAll();
    }

    public Page<Video> findAll(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    public Video saveVideo(Video video){
        videoRepository.save(video);
        return video;
    }

    public boolean deleteVideo(Long id){
        Optional<Video> video = videoRepository.findById(id);

        if (video.isPresent()){
            videoRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public void updateVideo(Video video, HttpServletRequest request) {
        video.setTitle(request.getParameter("title"));
        video.setDuration(request.getParameter("duration"));
        video.setDescription(request.getParameter("description"));
        video.setVideoUrl(request.getParameter("videoUrl"));
        videoRepository.save(video);
    }

    public Video findVideoById(Long id){
        Optional<Video> video = videoRepository.findById(id);
        if (video.isPresent()){
            return video.get();
        }else{
            return null;
        }
    }
}
