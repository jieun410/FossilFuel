package edu.example.springbootblog.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long commentId;

    @Column
    private String commentAuthor;

    @Column
    private String commentContent;

    @CreatedDate
    @Column
    private LocalDateTime commentCreatedAt;

    @LastModifiedDate
    @Column
    private LocalDateTime commentUpdatedAt;


    @Column
    @JsonProperty("commentIsHidden")  // 추가
    private boolean commentIsHidden;

    @Column
    @JsonProperty("commentIsDeleted")  // 추가
    private boolean commentIsDeleted;


    @ManyToOne // 여러 개의 InsertedFile이 하나의 post 속함
    @JoinColumn(name = "post_id", nullable = false) // 외래키 설정
    @JsonIgnore //순환 참조를 방지
    private Post post;

    @ManyToOne // 자기 참조, 대댓글을 위한 상위 댓글
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    private Comment parentComment; // 부모 댓글을 참조

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>(); // 자식 댓글들 (대댓글 리스트)

    public void addChildComment(Comment childComment) {
        this.childComments.add(childComment);
        childComment.changeParentComment(this);
    }


    public void changeParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void changeCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
    public void changeCommentIsHidden(boolean commentIsHidden) {
        this.commentIsHidden = commentIsHidden;
    }
    public void changeCommentIsDeleted(boolean commentIsDeleted) {
        this.commentIsDeleted = commentIsDeleted;
    }

    public void update(String commentContent) {
        this.commentContent = commentContent;

    }






}
