package edu.example.springbootblog.post.postDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 게시글 수정 요청 시 사용되는 DTO
public class UpdateArticleRequest {
    private String title;
    private String content;
}
