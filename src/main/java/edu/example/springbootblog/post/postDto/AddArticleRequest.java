package edu.example.springbootblog.post.postDto;

import edu.example.springbootblog.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
//클라이언트로부터 전달받은 게시글데이터를 담고 있는 DTO 클래스
public class AddArticleRequest {
    private String title;
    private String content;

    public Post toEntity(String author){
        return Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .viewCount(0L)
                .build();
    }
}
