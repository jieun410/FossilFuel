//package edu.example.springbootblog.controller;
//
//
//import edu.example.springbootblog.domain.Article;
//import edu.example.springbootblog.dto.apidto.AddArticleRequest;
//import edu.example.springbootblog.dto.apidto.ArticleResponse;
//import edu.example.springbootblog.dto.apidto.UpdateArticleRequest;
//import edu.example.springbootblog.service.BlogService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//public class BlogApiController4 { // 필터를 통해서 인증을 적용하고 여기서는 호출만 하려 했는데 안되네
//
//    private final BlogService blogService;
//    private static final Logger logger = LoggerFactory.getLogger(BlogApiController4.class);
//
//    @PostMapping("/api/articles")
//    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
//        Article saveArticle = blogService.save(request);
//        logger.info("글 작성 성공: {}", saveArticle);
//        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
//    }
//
//    @GetMapping("/api/articles")
//    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
//        List<ArticleResponse> articles = blogService.findAll()
//                .stream()
//                .map(ArticleResponse::new)
//                .toList();
//
//        logger.info("글 목록 조회 성공: 총 {}개의 글", articles.size());
//        return ResponseEntity.ok().body(articles);
//    }
//
//    @GetMapping("/api/articles/{id}")
//    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
//        Article article = blogService.findById(id);
//        logger.info("글 조회 성공: ID: {}", id);
//        return ResponseEntity.ok().body(new ArticleResponse(article));
//    }
//
//    @DeleteMapping("/api/articles/{id}")
//    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
//        blogService.delete(id);
//        logger.info("글 삭제 성공: ID: {}", id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/api/articles/{id}")
//    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
//        Article updatedArticle = blogService.update(id, request);
//        logger.info("글 수정 성공: ID: {}", id);
//        return ResponseEntity.ok().body(updatedArticle);
//    }
//}