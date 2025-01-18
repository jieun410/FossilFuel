package edu.example.springbootblog.global;


import edu.example.springbootblog.global.jwt.TokenAuthenticationFilter;
import edu.example.springbootblog.global.jwt.TokenProvider;
import edu.example.springbootblog.global.oauth.CustomLogoutHandler;
import edu.example.springbootblog.global.oauth.OAuth2SuccessHandler;
import edu.example.springbootblog.global.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import edu.example.springbootblog.global.repository.RefreshTokenRepository;
import edu.example.springbootblog.global.service.Oauth2UserCustomService;
import edu.example.springbootblog.user.service.UserDetailService;
import edu.example.springbootblog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig  {

    private final Oauth2UserCustomService oauth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    //스프링 시큐리티의 기본 보안 설정을 비활성화하는 부분
    // 이미지, CSS, JS 파일과 H2 데이터베이스 콘솔에 대한 요청은 보안 필터 체인을 통과하지 않고 바로 접근할 수 있도록 설정한다.
    @Bean
    public WebSecurityCustomizer configure() { //스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers("/img/**", "/css/**", "/js/**"); //정적 자원에 대한 경로를 보안 검사에서 제외
    }

    // 메인 보안 설정 메서드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 및 HTTP 기본 인증을 비활성화
        http.csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (API에서 주로 사용)
                .httpBasic(httpBasic -> httpBasic.disable())  // 기본 HTTP 인증 비활성화
                .formLogin(formLogin -> formLogin.disable())  // 기본 폼 로그인 비활성화
                .logout(logout -> logout.disable());  // 기본 로그아웃 비활성화

        // 세션을 사용하지 않는 방식으로 설정 (무상태)
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        // JWT 토큰을 처리하는 커스텀 필터를 추가 (UsernamePasswordAuthenticationFilter 앞에 배치)
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // API 토큰 발급 경로와 파일 업로드 경로는 인증 없이 접근 가능
        http.authorizeRequests()
                .requestMatchers("/api/token", "/api/upload/**", "/api/login", "/ws/**", "/api/blogs/**", "blogs/", "/new-blog", "/articles").permitAll() // 인증 필요 없음
                .requestMatchers("/api/**").authenticated()  // API 요청은 인증이 필요
                //  .requestMatchers("/mypage").authenticated() // Only authenticated users can access
                .anyRequest().permitAll();  // 그 외 모든 요청은 인증 필요 없음

        // OAuth2 로그인 설정: 로그인 성공 후 사용자 정보를 커스텀 핸들러로 처리
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")  // OAuth2 로그인 페이지 설정
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))  // OAuth2 요청을 쿠키에 저장
                .successHandler(oAuth2SuccessHandler())  // 로그인 성공 후 토큰 발급 처리
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oauth2UserCustomService)));  // 사용자 정보 서비스 설정

        // 로그아웃 설정: 커스텀 로그아웃 핸들러 사용
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .addLogoutHandler(new CustomLogoutHandler(oAuth2AuthorizationRequestBasedOnCookieRepository()))  // 커스텀 로그아웃 핸들러 추가
                .logoutSuccessUrl("/login"));  // 로그아웃 후 로그인 페이지로 리다이렉트

        // 예외 처리: 인증되지 않은 API 요청에 대해 401 에러 반환
        http.exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")));  // 인증되지 않은 API 요청에 대해 401 응답

        return http.build();  // 설정 완료 후 SecurityFilterChain 반환
    }

    // OAuth2 인증 성공 후 토큰을 발급하는 커스텀 핸들러
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService);
    }

    //JWT 토큰을 확인하고 인증하는 커스텀 필터
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, userDetailService);
    }

    // OAuth2 인증 요청을 쿠키에 기반하여 저장하고 관리하는 리포지토리
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository(){
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    //사용자 비밀번호를 암호화하기 위해 BCryptPasswordEncoder를 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//요약:
//이 코드는 Spring Security 6.1을 기반으로 OAuth2 로그인과 JWT 토큰 인증을 지원하는 보안 설정을 구현합니다.
// 세션을 사용하지 않고 무상태(stateless) 방식으로 동작하며, 특정 API 엔드포인트는 인증 없이 접근할 수 있도록 설정하고,
// OAuth2 로그인 성공 시 사용자 정보를 저장하거나 업데이트하는 처리를 담당합니다.