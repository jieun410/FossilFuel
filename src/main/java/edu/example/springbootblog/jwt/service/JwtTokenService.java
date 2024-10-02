package edu.example.springbootblog.jwt.service;


import edu.example.springbootblog.jwt.config.JwtUtil;
import edu.example.springbootblog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtUtil jwtUtil;
    private final UserDetailService userDetailService;
    private final ReissueService reissueService;

    // 엑세스 토큰 검증
    public boolean validateAccessToken(String accessToken) {
        return jwtUtil.validateToken(accessToken);
    }

    // 엑세스 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return jwtUtil.getEmail(token);
    }

    // 리프레시 토큰을 사용한 엑세스 토큰 재발급
    public String reissueAccessTokenByRefreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            return reissueService.reissueAccessTokenByRefreshToken(refreshToken).getAccessToken();
        }
        return null;
    }
}