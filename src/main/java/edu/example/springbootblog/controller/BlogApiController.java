package edu.example.springbootblog.controller;

import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.apidto.AddArticleRequest;
import edu.example.springbootblog.dto.apidto.ArticleResponse;
import edu.example.springbootblog.dto.apidto.UpdateArticleRequest;
import edu.example.springbootblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article saveArticle = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList(); // dto에 있는 아티클 리스폰스로 파싱해서 바디에 담는다.

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/api/articles/{id}") // 슬래시 하나 빼먹어서, 계속 에러
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}") // 여기서 사용된 DTO(UpdateArticleR) 에는 id가 없기에 @PathVariable 로 받음
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok().body(updatedArticle);
    }
}

