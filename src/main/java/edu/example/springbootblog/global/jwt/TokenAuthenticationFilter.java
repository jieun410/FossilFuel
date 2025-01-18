package edu.example.springbootblog.global.jwt;


import edu.example.springbootblog.user.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider; //JWT 토큰을 생성하고 검증하는 역할을 담당하는 클래스
    private final UserDetailService userDetailService;

    private final static String HEADER_AUTHORIZATION = "Authorization"; //Authorization 헤더의 키 값, 즉 클라이언트가 인증 토큰을 보낼 때 사용하는 HTTP 헤더 이름.
    private final static String TOKEN_PREFIX = "Bearer "; //JWT 토큰의 접두사, 일반적으로 "Bearer "로 사용



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader=request.getHeader(HEADER_AUTHORIZATION); // 요청 헤더에서 Authorization 헤더의 값을 가져온다. 여기에는 토큰이 포함됨
        String token = getAccessToken(authorizationHeader); //헤더에서 Bearer 접두사를 제거한 실제 JWT 토큰을 추출
        if(tokenProvider.validToken(token)){ //토큰의 유효성을 검사한다. 토큰이 유효하면 true를 반환
            Authentication auth = tokenProvider.getAuthentication(token); // 유효한 토큰인 경우, 토큰을 사용해 인증 정보를 가져온다.
            SecurityContextHolder.getContext().setAuthentication(auth); //인증 정보를 Spring Security의 SecurityContext에 저장한다. 이렇게 설정된 인증 정보는 이후 요청이 처리될 때 참고된다.
        }

        filterChain.doFilter(request, response); //필터 체인 내의 다음 필터로 요청을 전달함
    }

    private String getAccessToken(String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) { //헤더가 존재하고 Bearer로 시작하는지 확인
            return authorizationHeader.substring(TOKEN_PREFIX.length()); //Bearer 부분을 제거하고 순수한 토큰만 추출하여 반환
        }
        return null;
    }
}
