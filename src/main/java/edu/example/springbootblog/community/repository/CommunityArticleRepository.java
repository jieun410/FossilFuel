package edu.example.springbootblog.community.repository;

import edu.example.springbootblog.community.domain.CommunityArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityArticleRepository extends JpaRepository<CommunityArticle, Long> {
}