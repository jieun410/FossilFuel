package edu.example.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.springbootblog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
}