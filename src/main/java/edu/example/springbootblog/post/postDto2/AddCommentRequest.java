package edu.example.springbootblog.post.postDto2;

import edu.example.springbootblog.post.domain.Comment;
import edu.example.springbootblog.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {
    private String commentContent;
    private Long parentCommentId; // 대댓글일 경우 부모 댓글의 ID

    // Article과 Comment 객체를 이용해 Comment 엔티티 생성
    public Comment toEntity(String commentAuthor, Post article, Comment parentComment) {
        return Comment.builder()
                .commentAuthor(commentAuthor)
                .commentContent(commentContent)
                .post(article)
                .parentComment(parentComment) // parentComment는 대댓글일 경우에 사용됨
                .build();
    }
}