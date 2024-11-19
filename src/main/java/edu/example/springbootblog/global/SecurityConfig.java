package edu.example.springbootblog.global;


import edu.example.springbootblog.user.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig  {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public WebSecurityCustomizer config() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "login","signup").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 로그인 페이지 경로 설정
                        .defaultSuccessUrl("/articles")  // 로그인 성공 후 이동할 페이지
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")  // 로그아웃 성공 후 이동할 페이지
                        .invalidateHttpSession(true)  // 세션 무효화
                )
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (필요한 경우)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 401 상태 코드와 함께 JSON 응답 반환
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                );

        return http.build();  // 필터 체인 빌드
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       CustomUserDetailsService customUserDetailsService) throws Exception {
        // HttpSecurity에서 AuthenticationManagerBuilder 가져오기
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // 사용자 정보 서비스와 패스워드 인코더 설정
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService) // UserDetailsService를 설정하여 사용자 정보를 로드
                .passwordEncoder(bCryptPasswordEncoder); // 패스워드 인코더로 BCryptPasswordEncoder를 설정

        // 설정이 완료된 AuthenticationManagerBuilder에서 AuthenticationManager 빌드 및 반환
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}