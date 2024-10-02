package edu.example.springbootblog.jwt.controller;

import edu.example.springbootblog.dto.LoginRequest;
import edu.example.springbootblog.jwt.controller.dto.JwtInfoDto;
import edu.example.springbootblog.jwt.service.JwtLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class JwtLoginApiController {

    private final JwtLoginService jwtLoginService;
    private static final Logger logger = LoggerFactory.getLogger(JwtLoginApiController.class);

    @PostMapping("/api/login")
    public ResponseEntity<JwtInfoDto> login(@RequestBody LoginRequest loginRequestDto, HttpServletResponse response) {
        JwtInfoDto jwtInfoDto = jwtLoginService.login(loginRequestDto);

        // (Http 응답 안에) Authorization 헤더에 AccessToken 추가
        response.setHeader("Authorization", "Bearer " + jwtInfoDto.getAccessToken());

        // 쿠키 설정 의미 x ?
//        Cookie cookie = new Cookie("jwt", "Bearer+" + jwtInfoDto.getAccessToken());
//        cookie.setPath("/"); // 쿠키의 경로를 설정하여 "/" 이하의 모든 경로에서 접근 가능하도록
//        cookie.setSecure(true); // 쿠키가 HTTPS 연결을 통해서만 전송되도록 설정
//        cookie.setHttpOnly(true); // 쿠키에 대한 JavaScript 접근을 방지하여 보안을 강화
//        cookie.setMaxAge(60 * 25); // 쿠키의 유효 시간을 25분(1500초)으로 설정
//        response.addCookie(cookie); // 생성한 쿠키를 응답에 추가하여 클라이언트에 전송 -> 위 http 응답과 중복?


        // 쿠키 설정 예시
        Cookie cookie = new Cookie("accessToken", jwtInfoDto.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);  // XSS 공격 방지
        cookie.setSecure(true);    // HTTPS 연결 시에만 쿠키 전송 (개발 시에는 false로 설정 가능)
        cookie.setMaxAge(60 * 60); // 쿠키의 유효 기간 설정 (예: 1시간)
        response.addCookie(cookie);


        logger.info("Authorization 헤더에 AccessToken이 추가되었습니다: {}", jwtInfoDto.getAccessToken());
        logger.info("쿠키 생성 완료: 쿠키 이름 '{}', 만료 시간 {}", cookie.getName(), cookie.getMaxAge());
        return ResponseEntity.ok(jwtInfoDto) ; // 로그인 성공 후 사용자 서비스 페이지로 이동
    }
    // 뷰페이지를 이동시키는건, 성공핸들러에서 처리시키고
    // 여기서는  return ResponseEntity.ok(jwtInfoDto); 등을 반환시키는게 맞지 않을까?

//    @PostMapping("/login")
//    public ResponseEntity<JwtInfoDto> login(@RequestBody LoginRequestDto loginRequestDto) {
//        JwtInfoDto jwtInfoDto = authService.login(loginRequestDto);
//        return ResponseEntity.ok(jwtInfoDto);
//    }
}