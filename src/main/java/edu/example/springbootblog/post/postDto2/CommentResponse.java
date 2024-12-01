package edu.example.springbootblog.post.postDto2;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.example.springbootblog.post.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private  Long articleId;
    private String commentAuthor;
    private String commentContent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentCreatedAt;
    private Long commentId;
    private Long parentCommentId; // 부모 댓글의 ID
    private boolean commentIsHidden;
    private boolean commentIsDeleted;

    public CommentResponse(Comment comment) {
        this.articleId = comment.getPost().getId();
        this.commentAuthor=comment.getCommentAuthor();
        this.commentContent=comment.getCommentContent();
        this.commentCreatedAt=comment.getCommentCreatedAt();
        this.commentIsHidden = comment.isCommentIsHidden();
        this.commentIsDeleted = comment.isCommentIsDeleted();
        this.commentId=comment.getCommentId();
        this.parentCommentId= comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null; // 부모 댓글 ID 설정


    }
}
