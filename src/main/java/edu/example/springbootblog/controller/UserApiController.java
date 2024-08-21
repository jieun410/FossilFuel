package edu.example.springbootblog.controller;

import edu.example.springbootblog.dto.securedto.AddUserRequest;
import edu.example.springbootblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest addUserRequest) {
        userService.save(addUserRequest); // 회원가입 메소드 호출 (서비스 단에 작성한)
        return  "redirect:/login"; // 회원가입이 완료되면 로그인 페이지로 리 다이렉트
    }

}
