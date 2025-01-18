package edu.example.springbootblog.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//액세스 토큰을 새로 발급받기 위한 요청
public class CreateAccessTokenRequest {
    private String refreshToken;
}