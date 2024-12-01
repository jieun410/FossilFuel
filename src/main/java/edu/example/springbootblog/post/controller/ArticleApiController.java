package edu.example.springbootblog.post.controller;

import edu.example.springbootblog.post.domain.Post;
import edu.example.springbootblog.post.postDto.AddArticleRequest;
import edu.example.springbootblog.post.postDto.ArticleResponse;
import edu.example.springbootblog.post.postDto.UpdateArticleRequest;
import edu.example.springbootblog.post.service.ArticleService;
import edu.example.springbootblog.post.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
@Log4j2
public class ArticleApiController {

    private final ArticleService articleService;
    private final FileUploadService fileUploadService;

    // 게시글 등록 API (POST)
   @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   // @PostMapping()
    public ResponseEntity<Post> addArticle(
           @RequestPart("request") AddArticleRequest request, // 게시글 데이터
           Principal principal // 현재 로그인한 사용자의 정보
    ) {
        // Article을 먼저 저장 (이미지는 아직 저장하지 않음)
        Post savedArticle = articleService.save(request, principal.getName(), null);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle); // 저장된 게시글 반환
    }

    // 모든 게시글 조회 API (GET)
   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // 서버가 항상 JSON 형식으로 응답하도록 명시적 설정
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 사용자 이름 가져오기
        log.info("userName: {}", userName);
        // 데이터베이스에서 모든 게시글을 조회하여 ArticleResponse로 변환
//        List<ArticleResponse> articles = articleService.findAll()
//                .stream()
//                .map(ArticleResponse::new) // Article 엔티티를 ArticleResponse DTO로 변환
//                .toList();
        List<ArticleResponse> articles = articleService.getArticles();

        return ResponseEntity.ok().body(articles); // 조회된 게시글 리스트를 반환
    }

    // 특정 게시글 조회 API (GET)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleResponse> findArticleById(@PathVariable("id") Long id) {
        // 게시글을 조회하고 현재 사용자 정보 확인
        Post article = articleService.findById(id);
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 로그인된 사용자 이름 가져오기

        // 작성자와 현재 사용자를 비교하여 isOwner 값 설정
        boolean isOwner = article.getAuthor().equals(currentUserName);
        log.info("currentUserName: {}", currentUserName);
        log.info("isOwner: {}", isOwner);

        return ResponseEntity.ok().body(new ArticleResponse(article)); // 조회된 게시글을 반환
    }



    // 게시글 수정 API (PUT)
    // 게시글을 수정하고, 선택적으로 파일도 함께 수정
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updateArticle(
            @PathVariable("id") Long id, // 수정할 게시글 ID
            @RequestPart("request") UpdateArticleRequest request, // 게시글 수정 데이터
            @RequestPart(value = "files", required = false) List<MultipartFile> files // 선택적으로 수정할 파일 리스트
    ) {
        // 게시글과 파일을 업데이트하는 서비스 호출
        Post updatedArticle = articleService.update(id, request, files);
        return ResponseEntity.ok().body(updatedArticle); // 수정된 게시글 반환
    }

    // 게시글 삭제 API (DELETE)
    // 특정 게시글을 삭제
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteArticle(@PathVariable("id") Long id) {
//        articleService.delete(id); // 게시글 삭제 서비스 호출
//        return ResponseEntity.ok().build(); // 성공 시 200 OK 반환 -->응답 본문 없음
        try {
            articleService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Article deleted successfully");
            return ResponseEntity.ok().body(response);  // JSON 응답 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error deleting article"));
        }
    }


}
