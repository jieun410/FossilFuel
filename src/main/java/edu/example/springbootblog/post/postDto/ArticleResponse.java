package edu.example.springbootblog.post.postDto;

import edu.example.springbootblog.post.domain.Post;
import lombok.Getter;

@Getter
//클라이언트로 전달할 게시글 데이터를 담는 dto
public class ArticleResponse {
    private final String title;
    private final String content;


    public ArticleResponse(Post post) { //public ArticleResponse(String title,String content)랑 다름
        this.title = post.getTitle();
        this.content = post.getContent();
    }

}
