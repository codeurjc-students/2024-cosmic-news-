package es.codeurjc.cosmic_news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SPAController {
    
    @GetMapping({ "/front/**/{path:[^\\.]*}", "/{path:front[^\\.]*}","/front/" })
        public String redirect() {
        return "forward:/front/index.html";
    }
}
