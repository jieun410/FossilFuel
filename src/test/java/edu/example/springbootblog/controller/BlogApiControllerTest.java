package edu.example.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.AddArticleRequest;
import edu.example.springbootblog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// make this : Command + shift + T

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화 및 역직렬화
    // this class do, java object Convert JSON data
    // Serialization <-> deserialization
    // 자바에서는 객체를 사용하지만, http에서는 json을 사용하기에 서로 다른 형식이라 변환 작업 수행함
    // 이러한 작업들을 직렬화 및 역직렬화라고 한다.

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // when run, before test run
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }


    @DisplayName("addArticle : SUCCESS, blog Post add . ")
    @Test
    public void addArticle() throws Exception {
        // given pattern
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // Serialization : Object to JSON
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when pattern
        // 설정한 내용들을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .content(requestBody));

        // then pattern
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findALl Articles : ")
    @Test
    public void findAllArticles() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());
        // 블로그 글을 저장합니다.

        final ResultActions resultActions = mockMvc.perform((get(url)
                .accept(MediaType.APPLICATION_JSON)));
        // 블로그 글 목록 조회 API를 호출합니다.

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));

    }

    @DisplayName("findArticle: Blog article Read one")
    @Test
    public void findArticle() throws Exception {
        // given pattern
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when pattern
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then pattern
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }
}