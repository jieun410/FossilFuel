package edu.example.springbootblog.community.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_articles")
public class CommunityArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary Key with Auto Increment
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "author", nullable = true)
    private String author; // 작성자 정보 (작성자의 이름 또는 아이디)

    @Column(name = "view_count", nullable = false)
    private int viewCount; // 조회수

    @Column(name = "comment_count", nullable = false)
    private int commentCount; // 댓글 수

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ArticleStatus status; // 게시물 상태 (예: ACTIVE, INACTIVE, DELETED)

    @Column(name = "category")
    private String category; // 게시물 카테고리 (예: 자유게시판, 질문게시판)

    @CreatedDate // Entity가 생성된 시간
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // Entity가 수정된 시간
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public CommunityArticle(String title, String content, String author, String category) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.viewCount = 0; // 초기 조회수 0
        this.commentCount = 0; // 초기 댓글 수 0
        this.status = ArticleStatus.ACTIVE; // 기본 상태 ACTIVE
    }

    public void update(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

    public void changeStatus(ArticleStatus status) {
        this.status = status;
    }

    public enum ArticleStatus {
        ACTIVE, // 활성 상태
        INACTIVE, // 비활성 상태
        DELETED // 삭제된 상태
    }
}