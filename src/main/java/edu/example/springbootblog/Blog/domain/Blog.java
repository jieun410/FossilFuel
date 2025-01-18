package edu.example.springbootblog.Blog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // prime key auto 1+
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder // make the object==객체 of Builder pattern
    public Blog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;

    }

    @CreatedDate // save when Entity Created
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // save when Entity Modified
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name="view_count")
    private Long viewCount= 0L;

    public void isIncrementViewCount()
    {this.viewCount++;}

}
