package edu.example.springbootblog.post.postDto3;


import com.fasterxml.jackson.annotation.JsonFormat;
import edu.example.springbootblog.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserArticlesList {
    private Long id;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long viewCount;

    public UserArticlesList(Post article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.createdAt = article.getCreatedAt();
        this.viewCount = article.getViewCount();
    }


}
