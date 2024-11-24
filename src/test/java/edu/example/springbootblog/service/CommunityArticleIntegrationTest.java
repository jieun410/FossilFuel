//package edu.example.springbootblog.service;
//
//import edu.example.springbootblog.community.dto.CommunityArticleRequest;
//import edu.example.springbootblog.community.entity.CommunityArticle;
//import edu.example.springbootblog.community.repository.CommunityArticleRepository;
//import edu.example.springbootblog.community.service.CommunityArticleService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class CommunityArticleIntegrationTest {
//
//    @Autowired
//    private CommunityArticleService service;
//
//    @Autowired
//    private CommunityArticleRepository repository;
//
//    @Test
//    void createAndRetrieveArticle_shouldPersistInDatabase() {
//        // Given
//        CommunityArticleRequest request = new CommunityArticleRequest();
//        request.setTitle("Test Title");
//        request.setContent("Test Content");
//        request.setAuthor("Test Author");
//        request.setCategory("General");
//
//        // When
//        CommunityArticle article = service.createArticle(request);
//
//        // Then
//        CommunityArticle found = repository.findById(article.getId()).orElseThrow();
//        assertThat(found.getTitle()).isEqualTo("Test Title");
//        assertThat(found.getContent()).isEqualTo("Test Content");
//    }
//}