package edu.example.springbootblog.post.postDto3;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//사용자가 작성한 댓글의 게시물 조회 DTO
@Getter
@NoArgsConstructor
public class UserCommentedArticlesList {

    private Long id;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long viewCount;

    public UserCommentedArticlesList(Long id, String title, LocalDateTime createdAt, Long viewCount){
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.viewCount = viewCount;

    }


}
