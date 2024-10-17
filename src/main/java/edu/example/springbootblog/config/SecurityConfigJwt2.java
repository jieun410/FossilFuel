//package edu.example.springbootblog.config;
//
//
//import edu.example.springbootblog.jwt.config.JwtAuthenticationFilter;
//import edu.example.springbootblog.jwt.handler.CustomAccessDeniedHandler;
//import edu.example.springbootblog.jwt.handler.CustomAuthenticationEntryPoint;
//import edu.example.springbootblog.jwt.service.JwtTokenService;
//import edu.example.springbootblog.service.UserDetailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//@EnableWebSecurity
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfigJwt2 {
//
//    private final UserDetailService userDetailsService;
//    private final JwtTokenService jwtTokenService;
//    private final CustomAccessDeniedHandler accessDeniedHandler;
//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
//
//    private static final String[] AUTH_WHITELIST = {
//            "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/", "/login", "/signup",
//            "/api/v1/auth/**", "/swagger-ui/index.html#/", "/api/v1/jwt/reissue"
//    };
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // CSRF, CORS 설정 비활성화 및 기본 설정
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(Customizer.withDefaults())
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable);
//
//        // 세션 관리: 상태 없음으로 설정 (JWT 사용)
//        http
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // 예외 처리 설정: 인증 실패 및 접근 거부 핸들러
//        http.exceptionHandling(exceptionHandling -> exceptionHandling
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler));
//
//        // JwtAuthenticationFilter를 필터 체인에 추가하여 모든 요청 가로채기
//        http
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        // 경로별 접근 권한 설정
//        http
//                .authorizeHttpRequests(auth -> auth
//                       // .requestMatchers(AUTH_WHITELIST).permitAll() // 인증 없이 접근 가능한 경로
//                      //  .anyRequest().authenticated()); // 그 외 경로는 인증 필요
//                        .anyRequest().permitAll());
//
//
//        return http.build();
//    }
//
//    // JwtAuthenticationFilter를 빈으로 등록
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(userDetailsService, jwtTokenService);
//    }
//
//    // 비밀번호 암호화
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}