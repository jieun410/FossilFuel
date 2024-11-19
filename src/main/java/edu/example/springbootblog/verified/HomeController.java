package edu.example.springbootblog.verified;


import edu.example.springbootblog.article.domain.Article;
import edu.example.springbootblog.article.repository.BlogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    private final BlogRepository articleRepository;

    // Constructor-based dependency injection
    public HomeController(BlogRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        // Fetch articles from the database
        List<Article> articles = articleRepository.findAll();

        // Add the articles to the model
        model.addAttribute("articles", articles);

        return "mainPageForMember";
    }
}