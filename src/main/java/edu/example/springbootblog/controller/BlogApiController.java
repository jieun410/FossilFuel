package edu.example.springbootblog.controller;

import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.AddArticleRequest;
import edu.example.springbootblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
// 이 코드의 목적은 블로그 글을 작성하는 REST API 엔드포인트를 구현하는 것입니다.
// 클라이언트는 /api/articles 경로로 POST 요청을 보내 블로그 글을 작성할 수 있으며,
// 서버는 이 요청을 처리하여 데이터베이스에 저장한 후,
// 생성된 글 정보를 JSON 형식으로 클라이언트에게 반환합니다.
// 이 과정에서 필요한 모든 처리는 BlogService 클래스에 위임됩니다.
