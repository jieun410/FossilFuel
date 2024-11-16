package edu.example.springbootblog.article.dto.apidto;


import edu.example.springbootblog.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // add general constructor
@AllArgsConstructor // all filed value get parameter in constructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity(){
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
// use to build pattern, DTO -> Entity (convert)