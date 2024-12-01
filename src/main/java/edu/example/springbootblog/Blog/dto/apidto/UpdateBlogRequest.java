package edu.example.springbootblog.Blog.dto.apidto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateBlogRequest {
    private String title;
    private String content;
}
