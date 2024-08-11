package edu.example.springbootblog.repository;

import edu.example.springbootblog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {

}
