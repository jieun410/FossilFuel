package edu.example.springbootblog.post.repository.search;

import edu.example.springbootblog.post.postDto.ArticleListViewResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostSearchImpl implements PostSearch {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ArticleListViewResponse> searchDTO(Pageable pageable) {
        // JPQL 쿼리 문자열을 작성
        String queryString = "SELECT new edu.example.springbootblog.post.postDto.ArticleListViewResponse(p) " +
                "FROM Post p WHERE p.id > 0";

        // 결과 조회 (페이징 적용)
        List<ArticleListViewResponse> results = entityManager.createQuery(queryString, ArticleListViewResponse.class)
                .setFirstResult((int) pageable.getOffset())  // 페이징 처리 (첫 번째 결과 위치)
                .setMaxResults(pageable.getPageSize())      // 한 페이지에 표시할 최대 결과 개수
                .getResultList();

        // 전체 레코드 수 조회
        long total = entityManager.createQuery("SELECT COUNT(p) FROM Post p WHERE p.id > 0", Long.class)
                .getSingleResult();

        // 결과를 PageImpl로 감싸서 반환 (페이지네이션)
        return new PageImpl<>(results, pageable, total);
    }
}