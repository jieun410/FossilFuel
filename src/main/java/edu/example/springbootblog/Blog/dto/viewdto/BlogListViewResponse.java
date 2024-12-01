package edu.example.springbootblog.Blog.dto.viewdto;

import edu.example.springbootblog.Blog.domain.Blog;
import lombok.Data;

@Data
public class BlogListViewResponse {
    private final Long id;
    private final String title;
    private final String content;

    public BlogListViewResponse(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
    }
}// 뷰에게 데이터를 전달하기 위한 객체
