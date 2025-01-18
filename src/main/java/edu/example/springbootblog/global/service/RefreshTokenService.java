package edu.example.springbootblog.global.service;

import edu.example.springbootblog.global.domain.RefreshToken;
import edu.example.springbootblog.global.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    // 주어진 refreshToken을 이용해 RefreshToken 엔티티를 조회하는 메서드
    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new IllegalArgumentException("Unexpected token"));  // 토큰이 없으면 예외 발생
    }
}
