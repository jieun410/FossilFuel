package edu.example.springbootblog.Blog.dto.viewdto;

import edu.example.springbootblog.Blog.domain.Blog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class BlogViewResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public BlogViewResponse(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.createdAt = blog.getCreatedAt();
    }

}
