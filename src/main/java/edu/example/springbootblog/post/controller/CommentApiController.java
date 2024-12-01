package edu.example.springbootblog.post.controller;

import edu.example.springbootblog.post.domain.Comment;
import edu.example.springbootblog.post.postDto2.AddCommentRequest;
import edu.example.springbootblog.post.postDto2.CommentResponse;
import edu.example.springbootblog.post.postDto2.UpdateCommentRequest;
import edu.example.springbootblog.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/comment")
public class CommentApiController {
    private final CommentService commentService;
    //1. 게시글에 맞는 한개 댓글 생성
    @PostMapping("/{articleId}")
    public ResponseEntity<Comment> addComment(@PathVariable("articleId") Long articleId,
                                              @RequestBody AddCommentRequest request
                                                , Principal principal) {
        Comment savedComment=commentService.saveComment(request,articleId,principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }



    //2. 게시글에 달린 댓글 목록 조회 (시간순)
    @GetMapping(value = "/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentResponse>> findComments(@PathVariable("articleId") Long articleId) {
        List<CommentResponse> comments=commentService.getComments(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    //3. 게시글에 맞는 한개 댓글과 대댓글 조회
    @GetMapping(value = "/{articleId}/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentResponse>> findReComments(@PathVariable("articleId") Long articleId,@PathVariable("commentId") Long commentId) {
        List<CommentResponse> comments=commentService.getReComments(articleId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PutMapping(value = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateCommentRequest> updateComment(@PathVariable("commentId") Long commentId,
                                                              @RequestBody UpdateCommentRequest request) {

       log.info("API Request: commentId = {}, isDeleted = {}, content = {}", commentId, request.isCommentIsDeleted(), request.getCommentContent());

        UpdateCommentRequest updatedComment=commentService.updateComment(commentId,request);

        log.info("Updated Comment: commentId = {}, isDeleted = {}, content = {}", commentId, updatedComment.isCommentIsDeleted(), updatedComment.getCommentContent());

        return ResponseEntity.status(HttpStatus.OK).body( updatedComment);
    }

    //5. 댓글 삭제
    @DeleteMapping(value = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
