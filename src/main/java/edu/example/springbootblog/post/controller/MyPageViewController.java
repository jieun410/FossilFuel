package edu.example.springbootblog.post.controller;


import edu.example.springbootblog.post.postDto3.UserArticlesList;
import edu.example.springbootblog.post.postDto3.UserCommentsList;
import edu.example.springbootblog.post.service.ArticleService;
import edu.example.springbootblog.post.service.CommentService;
import edu.example.springbootblog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/mypage")
public class MyPageViewController {
    private final UserService userService;
    private final ArticleService articleService;  // 게시글 관련 서비스
    private final CommentService commentService;

    @GetMapping()  // "/articles" 경로로 GET 요청을 처리
    public String myPage(Model model) {

        String currentUserName =  SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentUserName", currentUserName);

        return "self/myMain";
    }

    @GetMapping("/articles")  // "/articles" 경로로 GET 요청을 처리
    public String myPageArticles(Model model) {

        String currentUserName =  SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentUserName", currentUserName);
        List<UserArticlesList> userArticlesLists = articleService.getUserAllArticles(currentUserName);

        model.addAttribute("userArticlesLists", userArticlesLists);
        return "self/articles";
    }

    @GetMapping("/comments")  // "/articles" 경로로 GET 요청을 처리
    public String myPageComments(Model model) {

        String currentUserName =  SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentUserName", currentUserName);
        List<UserCommentsList> userCommentsLists=commentService.getUserAllComments(currentUserName);

        model.addAttribute("userCommentsLists", userCommentsLists);

        return "self/comments";
    }

}
