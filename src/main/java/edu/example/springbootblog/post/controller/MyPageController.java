package edu.example.springbootblog.post.controller;


import edu.example.springbootblog.post.service.MyPageService;
import edu.example.springbootblog.user.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
public class MyPageController{

    private final MyPageService myPageService;

    @GetMapping("/mypage/edit")
    public String getUserProfile(Model model) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = myPageService.getUserByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("currentUserName", email);

        model.addAttribute("profileImage", user.getProfileImageAsBase64());

        return "self/mypageedit";
    }

    @PostMapping("/self/edit/updateProfileImage")
    public String updateProfileImage(@RequestParam("profileImage") MultipartFile profileImage) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        myPageService.updateProfileImage(email, profileImage);

        return "redirect:/mypage/edit"; // 이미지 변경 후 마이페이지로 리다이렉트
    }
}