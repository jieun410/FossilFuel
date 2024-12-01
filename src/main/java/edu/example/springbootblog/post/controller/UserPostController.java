package edu.example.springbootblog.post.controller;

import edu.example.springbootblog.post.postDto3.UserArticlesList;
import edu.example.springbootblog.post.postDto3.UserCommentedArticlesList;
import edu.example.springbootblog.post.postDto3.UserCommentsList;
import edu.example.springbootblog.post.service.ArticleService;
import edu.example.springbootblog.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserPostController {

    private ArticleService articleService;
    private CommentService commentService;

    //사용자가 작성한 게시글 목록 조회
    @GetMapping(value = "/api/user/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<UserArticlesList>> findUserAllArticles(Principal principal) {
        List<UserArticlesList> articles = articleService.getUserAllArticles(principal.getName());
        return ResponseEntity.ok(articles); // 조회된 게시글 리스트를 반환
    }

    //사용자가 작성한 댓글과 해당 게시물 목록 조회
    @GetMapping(value = "/api/user/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<UserCommentsList>> findUserAllComments(Principal principal) {
        List<UserCommentsList> comments = commentService.getUserAllComments(principal.getName());
        return ResponseEntity.ok(comments); // 조회된 게시글 리스트를 반환
    }

    //사용자가 댓글 작성한 게시물 조회
    @GetMapping(value = "/api/user/commentedArticles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<UserCommentedArticlesList>> findUserAllCommentedArticles(Principal principal) {
        List<UserCommentedArticlesList> comments = commentService.getUserAllArticlesAndComments(principal.getName());
        return ResponseEntity.ok(comments); // 조회된 게시글 리스트를 반환
    }
}