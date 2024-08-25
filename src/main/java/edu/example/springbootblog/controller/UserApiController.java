package edu.example.springbootblog.controller;

import edu.example.springbootblog.dto.securedto.AddUserRequest;
import edu.example.springbootblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {
// API 컨트롤로( 회원가입 메소드를 통해, 회원정보를 저장하긴함)
    // 그러나, 회원가입이 완료된 후 로그인 페이지로 리다이렉트 시키기 때문에, 반환은 스트링 => 컨트롤러(뷰파일)
    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest addUserRequest) {
        userService.save(addUserRequest); // 회원가입 메소드 호출 (서비스 단에 작성한)
        return  "redirect:/signup"; // 회원가입이 완료되면 로그인 페이지로 리 다이렉트
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/login";
   } // 리다이렉트를 다르게 지정해도 반응하지 않는 이유는
    // 스프링 시큐리티 컨피그에서 //.logoutSuccessUrl("/login")
    // 위와 같이 지정해 놔서 그러함
    // 저쪽을 주석처리하고 이쪽을 signup으로 지정해도 반응이 없다.
    // 컨피그 설정(지정) = 반응
    // 컨트롤로 지정 = 무반응
    // 컨피그지정 > 시큐리티 자동 > 컨트롤러(불필요, 무의미)

}
