package edu.example.springbootblog.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    // 로그인 페이지로 이동하는 메서드
    @GetMapping("/login")  // "/login" 경로로 GET 요청을 처리
    public String login() {
        // oauthLogin.html 템플릿으로 이동 (로그인 페이지)
        return "user/oauthLogin";
    }

    // 회원가입 페이지로 이동하는 메서드
    @GetMapping("/signup")  // "/signup" 경로로 GET 요청을 처리
    public String signup() {
        // signup.html 템플릿으로 이동 (회원가입 페이지)
        return "user/signup";
    }
}
