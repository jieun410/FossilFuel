package edu.example.springbootblog.dto.apidto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateArticleRequest {
    private String title;
    private String content;
}
