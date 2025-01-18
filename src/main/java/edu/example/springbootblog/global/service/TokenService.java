package edu.example.springbootblog.global.service;

import edu.example.springbootblog.global.jwt.TokenProvider;
import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenProvider tokenProvider; // JWT 토큰 생성과 검증을 담당하는 클래스, 여기에서 새로운 Access Token을 생성
    private final RefreshTokenService refreshTokenService; //Refresh Token을 관리하는 서비스,  Refresh Token으로부터 사용자 정보를 얻을 수 있다.
    private final UserService userService; //사용자 정보를 관리하는 서비스로, 유저 ID를 사용해 유저 정보를 가져옴

    //주어진 Refresh Token을 사용해 새로운 Access Token을 생성하는 역할
    public String createNewAccessToken(String refreshToken) {
        //토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) { //TokenProvider를 사용해 Refresh Token이 유효한지 검증
            throw new IllegalArgumentException("Unexpected token"); //Refresh Token이 유효하지 않으면 IllegalArgumentException 예외를 던져 잘못된 토큰임을 알림
        }

        Long userId=refreshTokenService.findByRefreshToken(refreshToken).getUserId(); //RefreshTokenService에서 전달받은 Refresh Token을 바탕으로 해당 유저의 userId를 찾아냄
        User user=userService.findById(userId); //UserService를 사용해 유저 ID로부터 실제 사용자 정보를 조회

        return tokenProvider.generateToken(user, Duration.ofHours(2)); //조회된 사용자 정보를 바탕으로 TokenProvider를 사용해 새로운 Access Token을 생성
    }
}
//코드 흐름
//클라이언트로부터 받은 Refresh Token을 사용해 토큰의 유효성을 검증합니다.
//유효한 Refresh Token이라면, 그 토큰과 연관된 사용자의 ID를 조회합니다.
//조회된 사용자 정보를 바탕으로 새로운 Access Token을 생성하여 반환합니다.
//핵심 포인트
//유효성 검증: validToken()을 통해 Refresh Token이 유효한지 먼저 확인하고, 유효하지 않으면 예외를 발생시킵니다.
//사용자 정보 조회: Refresh Token에 포함된 사용자 ID를 통해 해당 사용자의 정보를 찾습니다.
//새로운 Access Token 생성: 기존의 Access Token이 만료된 경우, Refresh Token을 통해 2시간짜리 새로운 Access Token을 발급합니다.
