package edu.example.springbootblog.post.postDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
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

// 페이징을 위한 dto
@Schema(description = "게시글 목록 조회 시 페이징 정보 지정")
public class PageRequestDTO {

    @Builder.Default
    private int page = 1; //페이지 번호 - 첫번째 페이지 0부터 시작

    //필드 설명
    @Schema(description = "한 페이지에 표시할 게시물의 수")
    @Builder.Default
    @Max(10)
    private int size=10; //한 페이지 게시물 수


    //페이지번호, 페이지 게시물 수 , 정렬 순서를 Pageble 객체로 반환
    public Pageable getPageable(Sort sort) {
        int pageNum=page<0?1:page-1;
        int sizeNum=size<=10?10:size;
        //int sizeNum=2;
        return PageRequest.of(pageNum, sizeNum, sort); // 페이지는 0부터 시작하므로 -1
    }
}
