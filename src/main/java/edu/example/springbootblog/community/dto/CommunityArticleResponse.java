package edu.example.springbootblog.community.dto;


import edu.example.springbootblog.community.domain.CommunityArticle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityArticleResponse {

    private Long id;           // 게시글 ID
    private String title;      // 제목
    private String content;    // 내용
    private String author;     // 작성자
    private String category;   // 카테고리
    private int viewCount;     // 조회수
    private int commentCount;  // 댓글 수
    private String status;     // 게시글 상태 (ACTIVE, INACTIVE, DELETED)
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일

    @Builder
    public CommunityArticleResponse(Long id, String title, String content, String author, String category,
                                    int viewCount, int commentCount, String status,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Entity -> DTO 변환 메서드,
    // 사용하지 않고 대신 컨트롤러에서 만든 메소드를 통해 엔티티를 dto 로 바꾼다
    // Request dto 와 다른 특징 
    public static CommunityArticleResponse fromEntity(CommunityArticle article) {
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