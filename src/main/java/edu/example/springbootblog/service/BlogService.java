package edu.example.springbootblog.service;

import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.AddArticleRequest;
import edu.example.springbootblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // add constructor what having final, @Notnull
@Service // make the bean
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    // Read ALL
    public List<Article> findAll(){
        return blogRepository.findAll();
        //jpa 지원 메서드 파인드올 호출하여, 아티클 테이블에 저장되어 있는 모든 데이터를 조회
    }
}
