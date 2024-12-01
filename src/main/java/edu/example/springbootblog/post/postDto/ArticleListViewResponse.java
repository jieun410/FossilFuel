package edu.example.springbootblog.post.postDto;

import edu.example.springbootblog.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleListViewResponse {
    private final long id;
    private final String title;
    private final String content;
    private String author;
    private LocalDateTime createdAt;
    private Long viewCount;


    public ArticleListViewResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
        this.viewCount = post.getViewCount();
    }
}
