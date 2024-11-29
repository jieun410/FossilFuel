package edu.example.springbootblog.article.controller;

import edu.example.springbootblog.article.domain.Article;
import edu.example.springbootblog.article.dto.viewdto.ArticleListViewResponse;
import edu.example.springbootblog.article.dto.viewdto.ArticleViewResponse;
import edu.example.springbootblog.article.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    @Value("${kakao.map.api.key}")
    private String kakaoApiKey;

    @GetMapping("/")
    public String articles(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);
        // 모델 내부 메서드를 통해 값을 모델에 저장한다. (글 리스트) 반환값은 뷰파일 이름을 적어 찾으라는 뜻이다.
        // 다른 파일은, 들어오는 요청에 대한 엔드포인트를 정의한 것

        return "article/mainPage"; // == mainPage.html
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
      Article article = blogService.findById(id);
      model.addAttribute("article", new ArticleViewResponse(article));

      return "article/articleSee";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());

        }else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "article/newArticle";
    }

    
}
