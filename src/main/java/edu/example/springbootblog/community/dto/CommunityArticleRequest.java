package edu.example.springbootblog.community.dto;


import edu.example.springbootblog.community.domain.CommunityArticle;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityArticleRequest {

    private String title;     // 제목
    private String content;   // 내용
    private String author;    // 작성자
    private String category;  // 카테고리

    @Builder
    public CommunityArticleRequest(String title, String content, String author, String category) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
    }

    // DTO -> Entity 변환 메서드
    public CommunityArticle toEntity() {
        return CommunityArticle.builder()
                .title(this.title)
                .content(this.content)
                .author(this.author)
                .category(this.category)
                .build();
    }
}