package edu.example.springbootblog.jwt.service;

import edu.example.springbootblog.jwt.config.JwtUtil;
import edu.example.springbootblog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;
    private final UserDetailService userDetailService;
    private final ReissueService reissueService;

    // 엑세스 토큰 검증
    public boolean validateAccessToken(String accessToken) {
        log.debug("엑세스 토큰 검증 중: {}", accessToken);
        boolean isValid = jwtUtil.validateToken(accessToken);
        log.info("엑세스 토큰 유효성: {}", isValid);
        return isValid;
    }

    // 엑세스 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        log.debug("엑세스 토큰에서 이메일 추출 중: {}", token);
        String email = jwtUtil.getEmail(token);
        log.info("추출된 이메일: {}", email);
        return email;
    }

    // 리프레시 토큰을 사용한 엑세스 토큰 재발급
    public String reissueAccessTokenByRefreshToken(String refreshToken) {
        log.debug("리프레시 토큰을 사용한 엑세스 토큰 재발급 시도 중: {}", refreshToken);
        if (jwtUtil.validateToken(refreshToken)) {
            String newAccessToken = reissueService.reissueAccessTokenByRefreshToken(refreshToken).getAccessToken();
            log.info("엑세스 토큰 재발급 성공: {}", newAccessToken);
            return newAccessToken;
        }
        log.warn("리프레시 토큰이 유효하지 않음: {}", refreshToken);
        return null;
    }
}