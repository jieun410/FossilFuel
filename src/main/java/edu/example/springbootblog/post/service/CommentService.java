package edu.example.springbootblog.post.service;

import edu.example.springbootblog.post.domain.Comment;
import edu.example.springbootblog.post.domain.Post;
import edu.example.springbootblog.post.postDto2.*;
import edu.example.springbootblog.post.postDto3.UserCommentedArticlesList;
import edu.example.springbootblog.post.postDto3.UserCommentsList;
import edu.example.springbootblog.post.repository.ArticleRepository;
import edu.example.springbootblog.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    //1. 게시글에 맞는 한개 댓글 생성
    public Comment saveComment(AddCommentRequest request, Long articleId, String userName) {
        Post article = articleRepository.findById(articleId).orElseThrow(()->new IllegalArgumentException("Article not found"));

        // parentCommentId가 있는 경우 부모 댓글을 조회
        Comment parentComment=null;
        if(request.getParentCommentId()!=null){
            parentComment = commentRepository.findById(request.getParentCommentId()).orElseThrow(()->new IllegalArgumentException("Parent commentnot found"));

        }

        Comment savedComment = commentRepository.save(request.toEntity(userName,article,parentComment));

        return savedComment;
    }

    //2. 게시글에 달린 댓글 목록 조회(페이징)
    public Page<CommentListViewReponse> getComments(Long articleId, CommentPageRequestDTO commentPageRequestDTO) {
        try{
            Sort sort= Sort.by("commentId").descending(); //pno(상품 번호)를 기준으로 내림차순(descending())으로 정렬- 최신 상품이 상위에 표시
            Pageable pageable=commentPageRequestDTO.getPageable(sort); //PageRequestDTO 객체를 사용하여 페이징 정보를 생성
            return  commentRepository.list(commentPageRequestDTO.getId(),pageable); //Pageable 객체를 인자로 받아서 상품 목록을 페이지 단위로 반환
        }catch (Exception e){
            log.error("--- "+e.getMessage());
            throw new RuntimeException("Review NOT Fetched", e);
        }
    }
    //2. 게시글에 달린 댓글 목록 조회
    public List<CommentResponse> getComments(Long articleId) {
        // log.info("Fetching comments for articleId: {}", articleId);
        //List<Comment> comments = commentRepository.findByArticleId(articleId);
        List<Comment> comments = commentRepository.findByArticleIdOrderByCommentIdAsc(articleId);
        //log.info("Found {} comments for articleId {}", comments.size(), articleId);
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }



    //2. 게시글에 맞는 한개 댓글과 대댓글 조회
    public List<CommentResponse> getReComments(Long articleId,Long commentId) {
        // log.info("Fetching comments for articleId: {}", articleId);
        List<Comment> comments = commentRepository.findParentAndChildCommentsByArticleId(articleId,commentId);
        //log.info("Found {} comments for articleId {}", comments.size(), articleId);
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }



    //4. 댓글 수정
    @Transactional
    public UpdateCommentRequest updateComment(Long commentId, UpdateCommentRequest request) {
        Comment updatedComment=commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("Comment not found"));

        // 로그 추가
      log.info("Before Update: commentId = {}, isDeleted = {}, content = {}", updatedComment.getCommentId(), updatedComment.isCommentIsDeleted(), updatedComment.getCommentContent());


        authorizeCommentAuthor(updatedComment);
        //댓글 블라인드 처리 업데이트
        updatedComment.changeCommentContent(request.getCommentContent());
        updatedComment.changeCommentIsDeleted(request.isCommentIsDeleted());
        updatedComment.changeCommentIsHidden(request.isCommentIsHidden());
        //updatedComment.update(request.getCommentContent(), request.isCommentIsDeleted());

        // 로그 추가: 업데이트 후 값을 확인
      log.info("After Update: commentId = {}, isDeleted = {}, content = {}", updatedComment.getCommentId(), updatedComment.isCommentIsDeleted(), updatedComment.getCommentContent());

        return new UpdateCommentRequest(updatedComment);
    }



    //5. 댓글 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(()->new IllegalArgumentException("Comment not found"));
        authorizeCommentAuthor(comment);
        commentRepository.delete(comment);
    }

    //댓글 개수 세기
    @Transactional(readOnly = true)
    public long getCommentCount(long articleId) {
        return commentRepository.countCommentsByArticleId(articleId);
    }

    //사용자가 작성한 댓글과 해당 게시물 목록 조회
    public List<UserCommentsList> getUserAllComments(String userName) {
        List<Comment> comments = commentRepository.findUserComments(userName);

        // Comment 엔티티에서 필요한 데이터를 가공하여 DTO로 변환
        return comments.stream()
                .map(comment -> new UserCommentsList(
//                        comment.getCommentId(),
                        comment.getCommentContent(),
                        comment.getCommentCreatedAt(),
                        comment.getPost().getTitle(),  // 게시글 제목 가져오기
                        comment.getPost().getId()
                ))
                .collect(Collectors.toList());
    }

    //사용자가 작성한 댓글의 게시물 조회
    public List<UserCommentedArticlesList> getUserAllArticlesAndComments(String userName) {
        List<Post> articles = commentRepository.findUserArticlesAndComments(userName);

        // Article 엔티티에서 필요한 데이터를 가공하여 DTO로 변환
        return articles.stream()
                .map(article -> new UserCommentedArticlesList(
                        article.getId(),
                        article.getTitle(),
                        article.getCreatedAt(),
                        article.getViewCount()  // 게시글 조회수 가져오기
                ))
                .collect(Collectors.toList());
    }



    // 게시글의 작성자를 확인하여 권한 검증
    private void authorizeCommentAuthor(Comment comment) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 로그인된 사용자 확인
        if (!comment.getCommentAuthor().equals(userName)) { // 작성자가 아니면 예외 발생
            throw new IllegalArgumentException("not authorized");
        }
    }

}
