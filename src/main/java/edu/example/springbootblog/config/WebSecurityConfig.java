package edu.example.springbootblog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailsService userService;

    // 1. 스프링 스큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web)-> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }// 이유: 스프링 시큐리티 모든 기능은 무거워서, 인증 인가를 모든 서비스 적용 X
    // * requestMatchers == 특정요청에 따른 url 액세스 설정



    // 2/ 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests() // 3. 인증, 인가에 대한 설정
                .requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin() // 4. form 기반 로그인 설정
                .loginPage("/login")// 로그인 페이지 경로 지정
                .defaultSuccessUrl("/articles") // 로그인 성공시 이동할 경로 지정
                .and()
                .logout() // 5. 로그아웃 설정
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable() // 6. CRSF 공격 방어, 비활성화
                .build();

        // ()' is deprecated since version 6.1 and marked for removal
        // 스프링 시큐리티 6.1 부터는 사용되지 않아서 제거될 예정이라는 경고 문구
    }

    // 7. 인증 관리자 관련한 설정 들
    @Bean
    public AuthenticationManager authenticationManager
    (HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
     UserDetailsService userDetailsService ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                //  8. 사용자 정보 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    // 9. 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


