//package edu.example.springbootblog.controller;
//
//import edu.example.springbootblog.domain.Article;
//import edu.example.springbootblog.dto.apidto.AddArticleRequest;
//import edu.example.springbootblog.dto.apidto.ArticleResponse;
//import edu.example.springbootblog.dto.apidto.UpdateArticleRequest;
//import edu.example.springbootblog.jwt.config.JwtUtil; // JWT 유틸리티 클래스 추가
//import edu.example.springbootblog.service.BlogService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//        import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//public class BlogApiController2 {
//
//    private final BlogService blogService;
//    private final JwtUtil jwtUtil; // JWT 유틸리티를 주입받음
//    private static final Logger logger = LoggerFactory.getLogger(BlogApiController2.class);
//
//    // 쿠키에서 AccessToken을 가져와서 검증하는 메소드
//    private boolean validateAccessToken(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("accessToken")) {
//                    String accessToken = cookie.getValue();
//                    // JWT 유효성 검증
//                    if (jwtUtil.validateToken(accessToken)) {
//                        logger.info("AccessToken 검증 성공: {}", accessToken);
//                        return true;
//                    } else {
//                        logger.warn("AccessToken 검증 실패: {}", accessToken);
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    // 토큰 검증 API
//    @GetMapping("/api/verify-token")
//    public ResponseEntity<String> verifyToken(HttpServletRequest request) {
//        if (validateAccessToken(request)) {
//            return ResponseEntity.ok("Token is valid");
//        } else {
//            return ResponseEntity.status(401).body("Invalid token");
//        }
//    }
//
//    @PostMapping("/api/articles")
//    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, HttpServletRequest httpServletRequest) {
//        if (!validateAccessToken(httpServletRequest)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
//        }
//        Article saveArticle = blogService.save(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
//    }
//
//    @GetMapping("/api/articles")
//    public ResponseEntity<List<ArticleResponse>> findAllArticles(HttpServletRequest httpServletRequest) {
//        if (!validateAccessToken(httpServletRequest)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
//        }
//        List<ArticleResponse> articles = blogService.findAll()
//                .stream()
//                .map(ArticleResponse::new)
//                .toList();
//
//        return ResponseEntity.ok().body(articles);
//    }
//
//    @GetMapping("/api/articles/{id}")
//    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id, HttpServletRequest httpServletRequest) {
//        if (!validateAccessToken(httpServletRequest)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
//        }
//        Article article = blogService.findById(id);
//
//        return ResponseEntity.ok().body(new ArticleResponse(article));
//    }
//
//    @DeleteMapping("/api/articles/{id}")
//    public ResponseEntity<Void> deleteArticle(@PathVariable long id, HttpServletRequest httpServletRequest) {
//        if (!validateAccessToken(httpServletRequest)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
//        }
//        blogService.delete(id);
//
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/api/articles/{id}")
//    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request, HttpServletRequest httpServletRequest) {
//        if (!validateAccessToken(httpServletRequest)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
//        }
//        Article updatedArticle = blogService.update(id, request);
//
//        return ResponseEntity.ok().body(updatedArticle);
//    }
//}