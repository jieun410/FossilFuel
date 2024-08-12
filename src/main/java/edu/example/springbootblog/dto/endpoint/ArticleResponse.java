package edu.example.springbootblog.dto.endpoint;

import edu.example.springbootblog.domain.Article;
import lombok.Getter;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        // 엔티티를 인수로 받는 생성자
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
// 응답을 위한 dto 코드
// dto 코드는 응답을 위한 (통신에 사용되기에) 컨트롤러 <-> 서비스(비즈니스계층) <-> 퍼시스턴스계층 다 쓰임

