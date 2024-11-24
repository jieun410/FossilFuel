package edu.example.springbootblog.community.controller;

import edu.example.springbootblog.community.domain.CommunityArticle;
import edu.example.springbootblog.community.service.CommunityArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CommunityArticleViewController {

    private final CommunityArticleService articleService;

    // 게시글 목록 페이지
    @GetMapping("/community-articles")
    public String getArticlesPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "community/articles"; // Thymeleaf 템플릿 파일명
    }

    // 게시글 상세 페이지
    @GetMapping("/community-articles/{id}")
    public String getArticleDetailPage(@PathVariable Long id, Model model) {
        CommunityArticle article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "community/article-detail"; // Thymeleaf 템플릿 파일명
    }


    // 게시글 작성 페이지
    @GetMapping("/community-articles/new")
    public String getCreateArticlePage() {
        return "community/article-create"; // Thymeleaf 템플릿 파일명
    }

    // 게시글 수정 페이지
    @GetMapping("/community-articles/{id}/edit")
    public String getEditArticlePage(@PathVariable Long id, Model model) {
        CommunityArticle article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "community/article-edit"; // Thymeleaf 템플릿 파일명
    }
}