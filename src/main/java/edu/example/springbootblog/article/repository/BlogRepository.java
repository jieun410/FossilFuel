package edu.example.springbootblog.article.repository;

import edu.example.springbootblog.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {

}
