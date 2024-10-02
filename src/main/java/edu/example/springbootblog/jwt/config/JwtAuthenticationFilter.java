package edu.example.springbootblog.jwt.config;

import edu.example.springbootblog.jwt.service.JwtTokenService;
import edu.example.springbootblog.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 엑세스 토큰 가져오기
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);

            // 토큰 유효성 검증
            if (jwtTokenService.validateAccessToken(accessToken)) {
                String email = jwtTokenService.getEmailFromToken(accessToken);
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                setAuthentication(userDetails);
            } else {
                // 엑세스 토큰 만료 시 리프레시 토큰을 사용한 재발급 처리
                handleExpiredAccessToken(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    // 엑세스 토큰 만료 시 재발급 처리
    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue();
                    String newAccessToken = jwtTokenService.reissueAccessTokenByRefreshToken(refreshToken);
                    if (newAccessToken != null) {
                        Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
                        newAccessTokenCookie.setHttpOnly(true);
                        newAccessTokenCookie.setPath("/");
                        response.addCookie(newAccessTokenCookie);
                    }
                }
            }
        }
    }

    // 인증 설정
    private void setAuthentication(UserDetails userDetails) {
        if (userDetails != null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}