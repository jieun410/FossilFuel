package edu.example.springbootblog.global.oauth;


import edu.example.springbootblog.global.domain.RefreshToken;
import edu.example.springbootblog.global.jwt.JwtPrincipal;
import edu.example.springbootblog.global.jwt.TokenProvider;
import edu.example.springbootblog.global.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import edu.example.springbootblog.global.repository.RefreshTokenRepository;
import edu.example.springbootblog.user.domain.Role;
import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token"; //리프레시 토큰이 저장될 쿠키의 이름으로 "refresh_token"이라는 이름을 사용한다.
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); //리프레시 토큰의 유효 기간을 14일로 설정한 상수
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); //액세스 토큰의 유효 기간을 1일로 설정한 상수
    public static final String REDIRECT_PATH = "/articles"; //인증 성공 후 사용자가 리다이렉트될 경로로 "/articles"로 설정되어 있다.

    //인증 처리에 필요한 의존성
    private final TokenProvider tokenProvider; // TokenProvider는 JWT 토큰을 생성하고,
    private final RefreshTokenRepository refreshTokenRepository; // RefreshTokenRepository는 리프레시 토큰을 저장하는 레포지토리이며,
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;  // UserService는 사용자 정보를 관리하는 서비스

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // OAuth2AuthenticationToken으로 캐스팅
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // 인증된 사용자의 정보를 OAuth2User 객체로부터 가져온다.
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        // 이메일 추출
        String email = null;

        // 구글 OAuth일 경우
        if (oAuth2User.getAttributes().containsKey("email")) {
            email = (String) oAuth2User.getAttributes().get("email");

            // 인증 객체 생성
            Role role = Role.ROLE_USER; // 사용자 역할
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            new JwtPrincipal(getEmailFromOAuth2User(oAuth2User)), // 새로운 JwtPrincipal 생성
                            null,
                            Arrays.asList(new SimpleGrantedAuthority(role.getAuthority()))
                    );

            // SecurityContext에 인증(토큰) 객체 저장
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);

        }
        // 카카오톡 OAuth일 경우
        else if (oAuth2User.getAttributes().containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
        }

        // 이메일을 찾지 못한 경우 예외 처리
        if (email == null) {
            throw new IllegalArgumentException("이메일을 찾을 수 없습니다.");
        }

        // 사용자 정보 조회
        User user = userService.findByEmail(email);

        //리프레시 토큰 생성 ->저장->쿠키에 저장
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken, user.getEmail());
        addRefreshTokenToCookie(request, response, refreshToken);

        //액세스 토큰 생성-> 패스에 액세스 토큰을 추가
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        //인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response); //OAuth2 인증과 관련된 설정값과 쿠키를 제거

        //리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }



    //생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    //사용자의 userId로 기존 리프레시 토큰이 있는지 확인하고, 있으면 업데이트하고, 없으면 새로 생성하여 저장
    private void saveRefreshToken(Long userId, String newRefreshToken, String email) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken, email));

        refreshTokenRepository.save(refreshToken);
    }

    //생성된 리프레시 토큰을 쿠키에 저장
    //기존에 동일한 이름의 쿠키가 존재하면 먼저 삭제한 후, 새로운 리프레시 토큰을 쿠키에 추가
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    //인증 관련 설정값, 쿠키 제거
    //기본적으로 부모 클래스인 SimpleUrlAuthenticationSuccessHandler의 clearAuthenticationAttributes 메서드를 호출하여 인증 관련 설정을 삭제하고,
    // 이후 쿠키에 저장된 OAuth2 인증 요청도 제거한다.
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    //액세스 토큰을 패스에 추가
    //REDIRECT_PATH는 리다이렉트될 경로("/articles")이고, 그 경로에 액세스 토큰을 쿼리 파라미터로 추가하여 최종 URL을 생성한다.
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    // 이메일 추출을 위한 메서드
    private String getEmailFromOAuth2User(OAuth2User oAuth2User) {
        String email = null;
        if (oAuth2User.getAttributes().containsKey("email")) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (oAuth2User.getAttributes().containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
        }
        return email;
    }


}