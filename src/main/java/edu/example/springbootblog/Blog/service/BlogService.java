package edu.example.springbootblog.Blog.service;

import edu.example.springbootblog.Blog.domain.Blog;
import edu.example.springbootblog.Blog.dto.apidto.AddBlogRequest;
import edu.example.springbootblog.Blog.dto.apidto.UpdateBlogRequest;
import edu.example.springbootblog.Blog.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor // add constructor what having final, @Notnull
@Service // make the bean
public class BlogService {
    private final BlogRepository blogRepository;

    public Blog save(AddBlogRequest request){
        return blogRepository.save(request.toEntity());
    }

    // Read ALL
    public List<Blog> findAll(){
        return blogRepository.findAll();
        //jpa 지원 메서드 파인드올 호출하여, 아티클 테이블에 저장되어 있는 모든 데이터를 조회
    }

    // Read 1
    public Blog findById(Long id){ // Long & long 둘 다 가능
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Not Found : " + id));
    }

    public void delete(Long id){
        blogRepository.deleteById(id);
        // 오늘 진행한 스프링과 다르게 모든 메소드가 JPA에 구현되어 있고 (우리는 그 인터페이스를 상속받은 상태)
    }

    @Transactional // 트랜잭션용 메서드
    public Blog update(long id, UpdateBlogRequest request){ // 설마 이거 하나?
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + id));

        blog.update(request.getTitle(), request.getContent(), LocalDateTime.now());
        return blog;
    }

    //게시글 조회수 증가
    public Blog getIncreaseViewCount(Long id) {
        Blog blog=blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        blog.isIncrementViewCount();  // 조회수 증가
        // 변경된 조회수를 저장
        return blogRepository.save(blog);
    }
}
