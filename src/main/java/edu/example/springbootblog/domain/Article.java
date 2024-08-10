package edu.example.springbootblog.domain;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // prime key auto 1+
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder // make the object==객체 of Builder pattern
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    protected Article() {} // basic constructor


    // Getter
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
