package edu.example.springbootblog.Blog.controller;

import edu.example.springbootblog.Blog.domain.Blog;
import edu.example.springbootblog.Blog.dto.apidto.AddBlogRequest;
import edu.example.springbootblog.Blog.dto.apidto.BlogResponse;
import edu.example.springbootblog.Blog.dto.apidto.UpdateBlogRequest;
import edu.example.springbootblog.Blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/blogs")
    public ResponseEntity<Blog> addBlog(@RequestBody AddBlogRequest request) {
        Blog saveBlog = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveBlog);
    }

    @GetMapping("/api/blogs")
    public ResponseEntity<List<BlogResponse>> findAllBlog() {
        List<BlogResponse> blog = blogService.findAll()
                .stream()
                .map(BlogResponse::new)
                .toList(); // dto에 있는 아티클 리스폰스로 파싱해서 바디에 담는다.

        return ResponseEntity.ok().body(blog);
    }

    @GetMapping("/api/blogs/{id}") // 슬래시 하나 빼먹어서, 계속 에러
    public ResponseEntity<BlogResponse> findBlog(@PathVariable long id) {
        Blog blog = blogService.findById(id);

        return ResponseEntity.ok().body(new BlogResponse(blog));
    }

    @DeleteMapping("/api/blogs/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable long id) {
        blogService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/blogs/{id}") // 여기서 사용된 DTO(UpdateArticleR) 에는 id가 없기에 @PathVariable 로 받음
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody UpdateBlogRequest request) {
        Blog updatedBlog = blogService.update(id, request);

        return ResponseEntity.ok().body(updatedBlog);
    }
}

