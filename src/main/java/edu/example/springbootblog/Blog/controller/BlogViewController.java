package edu.example.springbootblog.Blog.controller;

import edu.example.springbootblog.Blog.domain.Blog;
import edu.example.springbootblog.Blog.dto.viewdto.BlogListViewResponse;
import edu.example.springbootblog.Blog.dto.viewdto.BlogViewResponse;
import edu.example.springbootblog.Blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    @Value("${kakao.map.api.key}")
    private String kakaoApiKey;

    @GetMapping("/")
    public String blogs(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        List<BlogListViewResponse> blogs = blogService.findAll().stream()
                .map(BlogListViewResponse::new)
                .toList();
        model.addAttribute("blogs", blogs);
        // 모델 내부 메서드를 통해 값을 모델에 저장한다. (글 리스트) 반환값은 뷰파일 이름을 적어 찾으라는 뜻이다.
        // 다른 파일은, 들어오는 요청에 대한 엔드포인트를 정의한 것

        return "blog/mainPage"; // == mainPage.html
    }

    @GetMapping("/blogs/{id}")
    public String getBlog(@PathVariable Long id, Model model) {
      Blog blog = blogService.findById(id);
      blogService.getIncreaseViewCount(id);
      model.addAttribute("blog", new BlogViewResponse(blog));

      return "blog/blogSee";
    }

    @GetMapping("/new-blog")
    public String newBlog(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("blog", new BlogViewResponse());

        }else {
            Blog blog = blogService.findById(id);
            model.addAttribute("blog", new BlogViewResponse(blog));
        }
        return "blog/newBlog";
    }

    
}
