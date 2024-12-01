package edu.example.springbootblog.post.postDto2;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPageRequestDTO {

    //2. 필드 설명
    @Builder.Default
    @Min(1)
    private int page = 1; //페이지 번호 - 첫번째 페이지 0부터 시작

    //기본 요청 - 1페이지, 리뷰 5개
    @Builder.Default
    @Min(5)
    @Max(100)
    private int size=5; //한페이지 리뷰 수
    private Long id; //게시물 아이디

    //페이지번호, 페이지 게시물 수 , 정렬 순서를 Pageble 객체로 반환
    public Pageable getPageable(Sort sort) {

        return PageRequest.of(page-1,size,sort);
    }
}
