//package edu.example.springbootblog.service;
//
//import edu.example.springbootblog.community.dto.CommunityArticleRequest;
//import edu.example.springbootblog.community.entity.CommunityArticle;
//import edu.example.springbootblog.community.repository.CommunityArticleRepository;
//import edu.example.springbootblog.community.service.CommunityArticleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//class CommunityArticleServiceTest {
//
//    @InjectMocks
//    private CommunityArticleService service;
//
//    @Mock
//    private CommunityArticleRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createArticle_shouldSaveArticle() {
//        // Given
//        CommunityArticleRequest request = new CommunityArticleRequest();
//        request.setTitle("Test Title");
//        request.setContent("Test Content");
//        request.setAuthor("Test Author");
//        request.setCategory("General");
//
//        CommunityArticle savedArticle = CommunityArticle.builder()
//                .title(request.getTitle())
//                .content(request.getContent())
//                .author(request.getAuthor())
//                .category(request.getCategory())
//                .build();
//
//        when(repository.save(any(CommunityArticle.class))).thenReturn(savedArticle);
//
//        // When
//        CommunityArticle result = service.createArticle(request);
//
//        // Then
//        assertThat(result.getTitle()).isEqualTo("Test Title");
//        assertThat(result.getContent()).isEqualTo("Test Content");
//        verify(repository, times(1)).save(any(CommunityArticle.class));
//    }
//
//    @Test
//    void getArticleById_shouldReturnArticle() {
//        // Given
//        Long id = 1L;
//        CommunityArticle article = CommunityArticle.builder()
//                .title("Test Title")
//                .content("Test Content")
//                .author("Test Author")
//                .category("General")
//                .build();
//
//        when(repository.findById(id)).thenReturn(Optional.of(article));
//
//        // When
//        CommunityArticle result = service.getArticleById(id);
//
//        // Then
//        assertThat(result.getTitle()).isEqualTo("Test Title");
//        assertThat(result.getContent()).isEqualTo("Test Content");
//        verify(repository, times(1)).findById(id);
//    }
//
//    @Test
//    void getArticleById_shouldThrowExceptionWhenNotFound() {
//        // Given
//        Long id = 1L;
//        when(repository.findById(id)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(IllegalArgumentException.class, () -> service.getArticleById(id));
//        verify(repository, times(1)).findById(id);
//    }
//
//    @Test
//    void updateArticle_shouldUpdateArticle() {
//        // Given
//        Long id = 1L;
//        CommunityArticleRequest request = new CommunityArticleRequest();
//        request.setTitle("Updated Title");
//        request.setContent("Updated Content");
//        request.setCategory("Updated Category");
//
//        CommunityArticle article = CommunityArticle.builder()
//                .title("Old Title")
//                .content("Old Content")
//                .author("Test Author")
//                .category("General")
//                .build();
//
//        when(repository.findById(id)).thenReturn(Optional.of(article));
//        when(repository.save(any(CommunityArticle.class))).thenReturn(article);
//
//        // When
//        CommunityArticle result = service.updateArticle(id, request);
//
//        // Then
//        assertThat(result.getTitle()).isEqualTo("Updated Title");
//        assertThat(result.getContent()).isEqualTo("Updated Content");
//        assertThat(result.getCategory()).isEqualTo("Updated Category");
//        verify(repository, times(1)).save(article);
//    }
//
//    @Test
//    void deleteArticle_shouldChangeStatusToDeleted() {
//        // Given
//        Long id = 1L;
//        CommunityArticle article = CommunityArticle.builder()
//                .title("Test Title")
//                .content("Test Content")
//                .author("Test Author")
//                .category("General")
//                .build();
//
//        when(repository.findById(id)).thenReturn(Optional.of(article));
//
//        // When
//        service.deleteArticle(id);
//
//        // Then
//        assertThat(article.getStatus()).isEqualTo(CommunityArticle.ArticleStatus.DELETED);
//        verify(repository, times(1)).save(article);
//    }
//}