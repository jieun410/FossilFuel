//package edu.example.springbootblog.jwt.controller;
//
//
//
//import edu.example.springbootblog.dto.LoginRequest;
//import edu.example.springbootblog.jwt.controller.dto.JwtInfoDto;
//import edu.example.springbootblog.jwt.service.JwtLoginService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Controller
//@RequiredArgsConstructor
//public class JwtLoginController2 {
//
//    private final JwtLoginService jwtLoginService;
//    private static final Logger logger = LoggerFactory.getLogger(JwtLoginApiController.class);
//
//    // 로그인 API - 쿠키에 토큰 저장
//    @PostMapping("/api/login")
//    public ResponseEntity<JwtInfoDto> login(@RequestBody LoginRequest loginRequestDto, HttpServletResponse response) {
//        JwtInfoDto jwtInfoDto = jwtLoginService.login(loginRequestDto);
//
//        // 쿠키에 AccessToken 저장
//        Cookie accessTokenCookie = new Cookie("accessToken", jwtInfoDto.getAccessToken());
//        accessTokenCookie.setPath("/"); // 쿠키의 경로를 전체로 설정
//        accessTokenCookie.setHttpOnly(true); // JavaScript에서 접근하지 못하도록
//        accessTokenCookie.setSecure(true); // HTTPS에서만 쿠키 전송
//        accessTokenCookie.setMaxAge(60 * 25); // 쿠키 유효 기간: 25분
//        response.addCookie(accessTokenCookie); // 응답에 쿠키 추가
//
//        logger.info("AccessToken 쿠키에 저장 완료: {}", jwtInfoDto.getAccessToken());
//        return ResponseEntity.ok(jwtInfoDto); // 로그인 성공 시 JWT 정보 반환
//    }
//
//    // 쿠키에서 AccessToken을 가져오는 예시
//    @GetMapping("/api/verify-token")
//    public ResponseEntity<String> verifyToken(HttpServletRequest request) {
//        // 쿠키에서 AccessToken 가져오기
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("accessToken")) {
//                    String accessToken = cookie.getValue();
//                    // 토큰 검증 로직 추가 가능
//                    logger.info("쿠키에서 AccessToken 확인: {}", accessToken);
//                    return ResponseEntity.ok("Token is valid: " + accessToken);
//                }
//            }
//        }
//        return ResponseEntity.status(401).body("No token found");
//    }
//}