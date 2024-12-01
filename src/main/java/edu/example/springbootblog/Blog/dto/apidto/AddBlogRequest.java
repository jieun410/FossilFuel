package edu.example.springbootblog.Blog.dto.apidto;


import edu.example.springbootblog.Blog.domain.Blog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // add general constructor
@AllArgsConstructor // all filed value get parameter in constructor
@Getter
public class AddBlogRequest {

    private String title;
    private String content;

    public Blog toEntity(){
        return Blog.builder()
                .title(title)
                .content(content)
                .build();
    }
}
// use to build pattern, DTO -> Entity (convert)