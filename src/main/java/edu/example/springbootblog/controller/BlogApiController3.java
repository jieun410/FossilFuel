package edu.example.springbootblog.controller;

import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.apidto.AddArticleRequest;
import edu.example.springbootblog.dto.apidto.ArticleResponse;
import edu.example.springbootblog.dto.apidto.UpdateArticleRequest;
import edu.example.springbootblog.jwt.config.JwtUtil;
import edu.example.springbootblog.service.BlogService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController3 {

    private final BlogService blogService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(BlogApiController3.class);

    // 쿠키에서 AccessToken을 가져와서 검증하는 메소드
    private boolean validateAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    String accessToken = cookie.getValue();
                    // 프론트엔드에서 넘어온 토큰 정보 로깅
                    logger.info("프론트에서 받은 AccessToken: {}", accessToken);

                    // JWT 유효성 검증 및 만료 확인
                    if (jwtUtil.validateToken(accessToken)) {
                        if (!jwtUtil.isTokenExpired(accessToken)) {
                            logger.info("AccessToken 검증 성공: {}", accessToken);
                            return true;
                        } else {
                            logger.warn("AccessToken 만료: {}", accessToken);
                            return false; // 만료된 경우 false 반환
                        }
                    } else {
                        logger.warn("AccessToken 검증 실패: {}", accessToken);
                        return false;
                    }
                }
            }
        }
        logger.warn("AccessToken 쿠키가 존재하지 않음");
        return false;
    }



    // 토큰 검증 API
    @GetMapping("/api/verify-token")
    public ResponseEntity<String> verifyToken(HttpServletRequest request) {
        if (validateAccessToken(request)) {
            logger.info("토큰 검증 성공");
            return ResponseEntity.ok("Token is valid");
        } else {
            logger.warn("토큰 검증 실패");
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, HttpServletRequest httpServletRequest) {
        if (!validateAccessToken(httpServletRequest)) {
            logger.warn("글 작성 요청 시 토큰 검증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
        }
        Article saveArticle = blogService.save(request);
        logger.info("글 작성 성공: {}", saveArticle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(HttpServletRequest httpServletRequest) {
        if (!validateAccessToken(httpServletRequest)) {
            logger.warn("글 목록 요청 시 토큰 검증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
        }
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        logger.info("글 목록 조회 성공: 총 {}개의 글", articles.size());
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id, HttpServletRequest httpServletRequest) {
        if (!validateAccessToken(httpServletRequest)) {
            logger.warn("글 조회 요청 시 토큰 검증 실패 (ID: {})", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
        }
        Article article = blogService.findById(id);
        logger.info("글 조회 성공: ID: {}", id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id, HttpServletRequest httpServletRequest) {
        if (!validateAccessToken(httpServletRequest)) {
            logger.warn("글 삭제 요청 시 토큰 검증 실패 (ID: {})", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
        }
        blogService.delete(id);
        logger.info("글 삭제 성공: ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request, HttpServletRequest httpServletRequest) {
        if (!validateAccessToken(httpServletRequest)) {
            logger.warn("글 수정 요청 시 토큰 검증 실패 (ID: {})", id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 토큰 검증 실패 시 401 응답
        }
        Article updatedArticle = blogService.update(id, request);
        logger.info("글 수정 성공: ID: {}", id);
        return ResponseEntity.ok().body(updatedArticle);
    }
}