//package edu.example.springbootblog.jwt.config;
//
//import edu.example.springbootblog.jwt.service.JwtTokenService;
//import edu.example.springbootblog.service.UserDetailService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter { // 필터 2와 차이는 여기가 로깅 추가
//
//    private final UserDetailService userDetailService;
//    private final JwtUtil jwtUtil;
//    private final JwtTokenService jwtTokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 엑세스 토큰 가져오기
//        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String accessToken = authorizationHeader.substring(7);
//            log.debug("요청 헤더에서 엑세스 토큰 추출: {}", accessToken);
//
//            // 토큰 유효성 검증
//            if (jwtTokenService.validateAccessToken(accessToken)) {
//                log.info("유효한 엑세스 토큰. 사용자 인증 정보 설정 중...");
//                String email = jwtTokenService.getEmailFromToken(accessToken);
//                UserDetails userDetails = userDetailService.loadUserByUsername(email);
//                setAuthentication(userDetails);
//            } else {
//                // 엑세스 토큰 만료 시 리프레시 토큰을 사용한 재발급 처리
//                log.warn("엑세스 토큰 만료. 리프레시 토큰으로 재발급 시도...");
//                handleExpiredAccessToken(request, response);
//            }
//        } else {
//            log.debug("Bearer 토큰이 요청 헤더에 없음");
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    // 엑세스 토큰 만료 시 재발급 처리
//    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("refreshToken")) {
//                    String refreshToken = cookie.getValue();
//                    log.debug("리프레시 토큰으로 엑세스 토큰 재발급 중: {}", refreshToken);
//                    String newAccessToken = jwtTokenService.reissueAccessTokenByRefreshToken(refreshToken);
//                    if (newAccessToken != null) {
//                        log.info("엑세스 토큰 재발급 성공. 쿠키에 새 엑세스 토큰 설정 중...");
//                        Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
//                        newAccessTokenCookie.setHttpOnly(true);
//                        newAccessTokenCookie.setPath("/");
//                        response.addCookie(newAccessTokenCookie);
//                    } else {
//                        log.warn("엑세스 토큰 재발급 실패");
//                    }
//                }
//            }
//        } else {
//            log.warn("리프레시 토큰이 쿠키에 없음");
//        }
//    }
//
//    // 인증 설정
//    private void setAuthentication(UserDetails userDetails) {
//        if (userDetails != null) {
//            log.debug("인증 정보 설정 중: 사용자 = {}", userDetails.getUsername());
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("사용자 인증 완료: {}", userDetails.getUsername());
//        } else {
//            log.warn("사용자 인증 정보가 없음");
//        }
//    }
//}