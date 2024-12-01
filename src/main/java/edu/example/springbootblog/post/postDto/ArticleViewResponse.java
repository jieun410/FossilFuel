package edu.example.springbootblog.post.postDto;

import edu.example.springbootblog.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
//게시글을 조회할 때 클라이언트에게 반환할 데이터를 담기 위한 DTO
public class ArticleViewResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private boolean isOwner;

    public ArticleViewResponse(Post article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.createdAt = article.getCreatedAt();

    }

    public ArticleViewResponse(Post article,String currentUserName) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.createdAt = article.getCreatedAt();
        this.isOwner = currentUserName.equals(article.getAuthor());
    }
}
