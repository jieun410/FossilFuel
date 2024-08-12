package edu.example.springbootblog.controller;

import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.AddArticleRequest;
import edu.example.springbootblog.dto.ArticleResponse;
import edu.example.springbootblog.dto.UpdateArticleRequest;
import edu.example.springbootblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@RestController // In Object[Http Response Body], data => Converted to JSON format
public class BlogApiController {

    private final BlogService blogService;

    // if Http method is POST, URL => mapping Method
    @PostMapping("/api/articles")   // /api/articles -> addArticles()
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
                                      // 이 메서드는 클라이언트로부터 블로그 글 작성 요청을 받아들입니다.
                                     // @RequestBody -> mapping : AddArticleRequest
        // @RequestBody 어노테이션은 HTTP 요청 본문에 있는 데이터를 자바 객체로 변환하는 역할을 합니다.
        // @RequestBody AddArticleRequest request: 클라이언트가 전송한 JSON 데이터가 AddArticleRequest 객체에 매핑됩니다.

        Article saveArticle = blogService.save(request);
       // BlogService의 save 메서드를 호출하여 새로운 블로그 글을 저장합니다.
        // 이 메서드는 요청 데이터를 바탕으로 Article 객체를 생성하고,
        // 이를 데이터베이스에 저장한 후, 저장된 객체를 반환합니다.

        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
                // 응답코드 201 반환 후, 테이블에 저장되어 있는 객체를 반환한다.
        // ResponseEntity는 HTTP 응답을 나타내는 객체로, 응답 상태 코드와 본문을 포함합니다.
        // status(HttpStatus.CREATED): HTTP 상태 코드 201을 반환합니다.
            // 201은 요청이 성공적으로 처리되었으며, 서버에 새로운 리소스가 생성되었음을 의미합니다.
        // body(saveArticle): 생성된 블로그 글(saveArticle) 객체를 응답 본문으로 반환합니다.
    }

    // post는 생성:Create , Get은 조회 Read

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        // * ResponseEntity는 Spring Framework에서 HTTP 응답을 표현하기 위해 사용되는 클래스
        // * ArticleResponse는 제네릭 타입 매개변수입니다
        // 1. ResponseEntity<T>: 제네릭 타입 T를 사용, HTTP 응답의 본문에 어떤 타입의 데이터를 포함할지 결정
        // 2. 여기서 T는 List<ArticleResponse>이므로,
        // 3. HTTP 응답 본문에는 ArticleResponse 객체의 리스트가 포함
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList(); // dto에 있는 아티클 리스폰스로 파싱해서 바디에 담는다.

        return ResponseEntity.ok() // Http 상태 코드 사용
                .body(articles);
        // 위 리턴문은, 200 OK 상태 코드와 함께 articles(리스트)를 응답 본문으로 포함하여 반환합니다.
    }

    @GetMapping("/api/articles/{id}") // 슬래시 하나 빼먹어서, 계속 에러
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(new ArticleResponse(article));
        // @PathVariable get value when URL
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }
}
// create
// 이 코드의 목적은 블로그 글을 작성하는 REST API 엔드포인트를 구현하는 것입니다.
// 클라이언트는 /api/articles 경로로 POST 요청을 보내 블로그 글을 작성할 수 있으며,
// 서버는 이 요청을 처리하여 데이터베이스에 저장한 후,
// 생성된 글 정보를 JSON 형식으로 클라이언트에게 반환합니다.
// 이 과정에서 필요한 모든 처리는 BlogService 클래스에 위임됩니다.

// Read all
// ResponseEntity<List<ArticleResponse>>는 HTTP 응답을 표현할 때
// List<ArticleResponse> 타입의 데이터를 본문으로 포함하여 반환하는 타입입니다.
// 제네릭을 통해 타입 안전성을 확보하고, List 컬렉션을 사용하여 ,
// ArticleResponse 객체들을 순서대로 담아 클라이언트에게 전달합니다.
