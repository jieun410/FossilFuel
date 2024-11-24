package edu.example.springbootblog.community.controller;

import edu.example.springbootblog.community.dto.CommunityArticleRequest;
import edu.example.springbootblog.community.dto.CommunityArticleResponse;
import edu.example.springbootblog.community.domain.CommunityArticle;
import edu.example.springbootblog.community.service.CommunityArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
// HTTP Body 에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
@RequestMapping("/api/community-articles")
@RequiredArgsConstructor
public class CommunityArticleController {

    private final CommunityArticleService articleService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<CommunityArticleResponse> createArticle(@RequestBody CommunityArticleRequest request) {
        CommunityArticle entity = articleService.createArticle(request);
        CommunityArticleResponse dto = toResponse(entity);
        return ResponseEntity.ok(dto);
    }
    // 1. Http Method @POST
    // 2. ResponseEntity<반환타입을 감싸서 반환> 사용으로 Http 응답 속도 증가
    // 3. @RequestBody 로 JSON 데이터를, class 파일(dto)에 매핑
    // 4. (dto 객체 서비스 단으로 보내고) 반환값 entity 저장
    //      * 서비스 단에서는, dto 안에 있는 메소드를 통해 dto -> entity 변환, 리포지터리 전달
    //      * 리포지터리 단에서 변환된 entity 객체 DB에 저장(이때, ORM:JPA 를 통해 추상화=쿼리 작성x)
    // 5. 최하단 메소드를 통해 entity -> dto 전환
    // 6. return 을 통해 .ok 200 & dto 반환

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<CommunityArticleResponse>> getArticles() {
        List<CommunityArticle> entity = articleService.getAllArticles();
        List<CommunityArticleResponse> dto = entity.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }


    // 게시글 단건 조회
    // @PathVariable 은 URL 에서 값을 가져오는 애너테이션
    @GetMapping("/{id}")
    public ResponseEntity<CommunityArticleResponse> getArticle(@PathVariable Long id) {
        CommunityArticle entity = articleService.getArticleById(id);
        CommunityArticleResponse dto = toResponse(entity);
        return ResponseEntity.ok(dto);
    }

    // 게시글 삭제 (상태 변경)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();

    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommunityArticleResponse> updateArticle(@PathVariable Long id, @RequestBody CommunityArticleRequest request) {
        CommunityArticle entity = articleService.updateArticle(id, request);
        CommunityArticleResponse dto = toResponse(entity);
        return ResponseEntity.ok(dto);
    }



    // Helper Method to convert Entity to Response DTO
    private CommunityArticleResponse toResponse(CommunityArticle article) {
        return CommunityArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .category(article.getCategory())
                .viewCount(article.getViewCount())
                .commentCount(article.getCommentCount())
                .status(article.getStatus().name())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}