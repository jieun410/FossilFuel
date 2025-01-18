package edu.example.springbootblog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 서버가 클라이언트에게 새로운 액세스 토큰을 반환할 때 사용되는 응답 DTO
public class CreateAccessTokenResponse {
    private String accessToken;
}