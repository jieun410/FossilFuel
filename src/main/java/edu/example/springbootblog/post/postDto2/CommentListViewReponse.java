package edu.example.springbootblog.post.postDto2;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.example.springbootblog.post.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentListViewReponse {
    private final Long commentId;
    private final String commentAuthor;
    private final String commentContent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentCreatedAt;
    private final Long articleId; //게시글 아이디
    private final Long parentCommentId; // 부모 댓글의 ID

    public CommentListViewReponse(Comment comment) {
        this.commentId=comment.getCommentId();
        this.commentAuthor=comment.getCommentAuthor();
        this.commentContent=comment.getCommentContent();
        this.commentCreatedAt=comment.getCommentCreatedAt();
        this.articleId=comment.getPost().getId();
        this.parentCommentId= comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null; // 부모 댓글 ID 설정
    }


}
