package edu.example.springbootblog.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "user/find-password";
    }

    @GetMapping("/find-username")
    public String findUsername() {
        return "user/find-username";
    }
}
