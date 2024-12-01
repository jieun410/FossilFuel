package edu.example.springbootblog.post.controller;

import edu.example.springbootblog.post.domain.Post;
import edu.example.springbootblog.post.postDto.ArticleListViewResponse;
import edu.example.springbootblog.post.postDto.ArticleViewResponse;
import edu.example.springbootblog.post.postDto.PageRequestDTO;
import edu.example.springbootblog.post.postDto2.CommentListViewReponse;
import edu.example.springbootblog.post.postDto2.CommentPageRequestDTO;
import edu.example.springbootblog.post.service.ArticleService;
import edu.example.springbootblog.post.service.CommentService;
import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Log4j2
public class ArticleViewController {
    private final ArticleService articleService;  // 게시글 관련 서비스
    private final UserService userService;
    private final CommentService commentService;

    // 게시글 목록을 페이지 네이션과 함께 가져오기
    @GetMapping("/articles")  // "/articles" 경로로 GET 요청을 처리
    public String getArticles(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {

        // 페이지 요청 정보에 맞는 게시글 리스트 가져오기 (페이지네이션 적용)
        Page<ArticleListViewResponse> articleListPage = articleService.getList(pageRequestDTO);
        // 현재 페이지의 게시글 목록을 모델에 추가
        model.addAttribute("articles", articleListPage.getContent());

        // 페이지 네이션 관련 정보를 모델에 추가
        model.addAttribute("page", articleListPage);

        // articleList.html 템플릿으로 리턴 (게시글 목록 페이지)
        return "articleList";
    }

    // 특정 게시글을 가져와서 보여줌
    @GetMapping("/articles/{id}")  // "/articles/{id}" 경로로 GET 요청을 처리
    public String getArticle(@PathVariable Long id, @ModelAttribute CommentPageRequestDTO commentPageRequestDTO, Model model) {
        // ID에 해당하는 게시글 찾기
        Post article = articleService.findById(id);


        articleService.getIncreaseViewCount(id); // 변경된 조회수를 저장

        Page<CommentListViewReponse> commentListPage = commentService.getComments(id,commentPageRequestDTO);

        long commentCount = commentService.getCommentCount(id);// 조회수

        // 현재 사용자 정보 가져오기 (로그인한 사용자의 이름 또는 이메일)
        String currentUserName =  SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authentication: {}",  SecurityContextHolder.getContext().getAuthentication());
        // 현재 사용자가 게시글의 작성자인지 확인
        boolean isArticleOwner = article.getAuthor().equals(currentUserName);

        log.info("currentUserName: {}", currentUserName);
        log.info("articleAuthor:{}", article.getAuthor());
        //log.info("isOwner: {}", isOwner);
        User articleUser=userService.findByEmail(article.getAuthor());
        String currrentUserImage=userService.findByEmail(currentUserName).getProfileImageAsBase64();

        // 게시글 정보를 모델에 추가
        model.addAttribute("article", article);
        model.addAttribute("profileImage", articleUser.getProfileImageAsBase64());
        model.addAttribute("isArticleOwner", isArticleOwner);
        model.addAttribute("currentUserName", currentUserName);
        model.addAttribute("currrentUserImage", currrentUserImage);

        model.addAttribute("comments", commentListPage.getContent());
        model.addAttribute("page", commentListPage);
        model.addAttribute("commentCount", commentCount);

        // article.html 템플릿으로 리턴 (게시글 상세 페이지)
        return "article";
    }

    // 새 게시글 작성 또는 수정 페이지로 이동
    @GetMapping("/new-article")  // "/new-article" 경로로 GET 요청을 처리
    // id가 있으면 해당 게시글을 수정, 없으면 새 글 작성
    public String newAticle(@RequestParam(required=false)Long id, Model model) {
        if (id == null) {
            // ID가 없으면 빈 게시글 객체를 모델에 추가 (새 글 작성)
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            // ID가 있으면 해당 게시글 정보를 조회해서 모델에 추가 (게시글 수정)
            Post article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        // newBlog.html 템플릿으로 리턴 (새 글 작성/수정 페이지)
        return "newArticle";
    }





}
