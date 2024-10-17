package edu.example.springbootblog.jwt.controller;

import edu.example.springbootblog.domain.RefreshToken;
import edu.example.springbootblog.dto.LoginRequest;
import edu.example.springbootblog.jwt.controller.dto.JwtInfoDto;
import edu.example.springbootblog.jwt.service.JwtLoginService;
import edu.example.springbootblog.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.authentication.AuthenticationManager;


@Controller
@RequiredArgsConstructor
public class JwtLoginApiController {

    private final JwtLoginService jwtLoginService;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtLoginApiController.class);
    private final AuthenticationManager authenticationManager;  // Inject AuthenticationManager


    @PostMapping("/api/login")
    public ResponseEntity<JwtInfoDto> login(@RequestBody LoginRequest loginRequestDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        System.out.println(authentication);

        JwtInfoDto jwtInfoDto = jwtLoginService.login(loginRequestDto);
        // 일단 여기서 로그인 하면, 설정해 놓은 프로퍼티스대로 엑세스&리프레쉬 발급함

        // (Http 응답 안에) Authorization 헤더에 AccessToken 추가
        response.setHeader("Authorization", "Bearer " + jwtInfoDto.getAccessToken());
        // 리프레쉬는 어떻게 반응? 클라이언트에게 줄까?
        refreshTokenRepository.save(RefreshToken.builder()
                .username(loginRequestDto.getUsername())
                .refreshToken(jwtInfoDto.getRefreshToken())
                .build());
        // 이거 잘 짠게 맞을까?




        // 쿠키 설정 예시 (지금까지 리프레쉬 토큰은 생성을 안하고 있었네 )
        Cookie cookie1 = new Cookie("accessToken", jwtInfoDto.getAccessToken());
       // Cookie cookie2 = new Cookie("refreshToken", jwtInfoDto.getRefreshToken());
        cookie1.setPath("/");
        cookie1.setHttpOnly(true);  // XSS 공격 방지
        cookie1.setSecure(true);    // HTTPS 연결 시에만 쿠키 전송 (개발 시에는 false로 설정 가능)
        cookie1.setMaxAge(60); // 쿠키의 유효 기간 설정 (예: 1시간: 60*60)
        // -> 1분
        response.addCookie(cookie1);

        logger.info("Authorization 헤더에 AccessToken이 추가되었습니다: {}", jwtInfoDto.getAccessToken());
        logger.info("RefreshToken 확인 : {}", jwtInfoDto.getRefreshToken());
        logger.info("쿠키 생성 완료: 쿠키 이름 '{}', 만료 시간 {}", cookie1.getName(), cookie1.getMaxAge());
       // logger.info("쿠키 생성 완료: 쿠키 이름 '{}', 만료 시간 {}", cookie2.getName(), cookie2.getMaxAge());
        logger.info("리프레쉬 토큰 생성 완료 : {}", refreshTokenRepository.findByRefreshToken(jwtInfoDto.getRefreshToken()));
        // 로그가 정상으로 찍히면, 위에서 저장 + 여기서 조회가 정상적으로 이루어진 것
        return ResponseEntity.ok(jwtInfoDto) ; // 로그인 성공 후 사용자 서비스 페이지로 이동
    }

}