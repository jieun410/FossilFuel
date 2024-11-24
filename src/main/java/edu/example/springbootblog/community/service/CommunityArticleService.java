package edu.example.springbootblog.community.service;

import edu.example.springbootblog.community.dto.CommunityArticleRequest;
import edu.example.springbootblog.community.domain.CommunityArticle;
import edu.example.springbootblog.community.repository.CommunityArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityArticleService {

    private final CommunityArticleRepository articleRepository;

    // 게시글 생성
    public CommunityArticle createArticle(CommunityArticleRequest request) {
        return articleRepository.save(request.toEntity());
    }

    // 목록 조회
    public List<CommunityArticle> getAllArticles() {
        return articleRepository.findAll();
    }

    // 단건 조회
    public CommunityArticle getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + id));
    }

    // 삭제 (상태 변경)
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);

    }

    // 수정
    @Transactional
    public CommunityArticle updateArticle(Long id, CommunityArticleRequest request) {
        CommunityArticle entity = getArticleById(id);
        entity.update(request.getTitle(), request.getContent(), request.getCategory());
        return articleRepository.save(entity);
    }


}