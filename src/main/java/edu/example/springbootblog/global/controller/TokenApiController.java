package edu.example.springbootblog.global.controller;

import edu.example.springbootblog.global.service.TokenService;
import edu.example.springbootblog.user.dto.CreateAccessTokenRequest;
import edu.example.springbootblog.user.dto.CreateAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    // 토큰 관련 비즈니스 로직을 처리하는 TokenService
    private final TokenService tokenService;

    // POST 요청을 처리하여 새로운 Access Token을 발급함
    @PostMapping("/api/token")  // /api/token 경로로 POST 요청이 들어오면 처리
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        // 클라이언트로부터 받은 Refresh Token으로 새로운 Access Token을 생성
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        // 새로운 Access Token을 담아 응답으로 보냄 (HTTP 상태 코드는 201 Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }
}