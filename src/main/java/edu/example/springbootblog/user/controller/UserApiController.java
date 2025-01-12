package edu.example.springbootblog.user.controller;

import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.dto.AddUserRequest;
import edu.example.springbootblog.user.dto.LoginRequest;
import edu.example.springbootblog.user.service.UserDetailService;
import edu.example.springbootblog.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Arrays;


@Controller
@RequiredArgsConstructor
@Log4j2
public class UserApiController {
    private final UserService userService;  // 사용자 서비스 클래스 (회원가입, 로그아웃 처리)
    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder passwordEncoder; // 비밀번호 암호화 검증을 위해 추가



    // 회원가입 요청 처리 메서드
    @PostMapping("/user") // HTTP POST 요청으로 회원가입 처리
    public String signup(AddUserRequest request){
        userService.save(request); // UserService를 통해 회원가입 로직 수행


        return "user/oauthLogin";


    }



    // 로그인 요청 처리
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // 1. 사용자 조회
        User user = (User) userDetailService.loadUserByUsername(loginRequest.getUsername());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("로그인 실패: 사용자 이름 또는 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // 2. SecurityContext에 사용자 인증 정보 설정
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")) // 권한 추가
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);

        // 3. 세션에 SecurityContext 저장
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", context);

        log.info("로그인 성공: 사용자 {}가 인증되었습니다.", user.getUsername());
        return ResponseEntity.ok().body("Login successful");
    }

    // 로그아웃 요청 처리 메서드
    @GetMapping("/logout") // HTTP GET 요청으로 로그아웃 처리
    public String logout(HttpServletRequest request, HttpServletResponse response){
        log.info("로그아웃 요청 수신");  // 로그아웃 요청이 들어왔음을 로깅

        // SecurityContextHolder를 통해 현재 로그인한 사용자의 이메일을 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("로그아웃 이메일: " + email);

        // UserService를 통해 해당 사용자의 닉네임을 null로 설정
        userService.setNicknameNullByEmail(email);
        log.info("닉네임 null로 설정 완료");

        // Spring Security의 로그아웃 처리 메서드 호출 (현재 사용자의 세션 및 인증 정보 삭제)
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        // 로그아웃 완료 후 로그인 페이지로 리다이렉트

        return "user/oauthLogin";


    }


    //사용자 탈퇴
    @DeleteMapping("/api/user")
    public ResponseEntity<Void> deleteUser() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUserByUsername(currentUserName);
        return ResponseEntity.ok().build();
    }





}