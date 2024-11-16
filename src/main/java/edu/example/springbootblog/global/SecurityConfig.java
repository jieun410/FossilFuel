//package edu.example.springbootblog.global;
//
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    //스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer config() {
//        return (web)->web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/static/**");
//    }
//
//    //특정 HTTP요청에 대한 웹 기반 보완 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/login", "/signup", "/user").permitAll()  // 인증 없이 접근 허용
//                        .anyRequest().authenticated()  // 나머지는 인증 필요
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")  // 로그인 페이지 경로 설정
//                        .defaultSuccessUrl("/articles")  // 로그인 성공 후 이동할 페이지
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login")  // 로그아웃 성공 후 이동할 페이지
//                        .invalidateHttpSession(true)  // 세션 무효화
//                        // "세션"은 웹 애플리케이션에서 클라이언트(사용자)와 서버 간의 상태를 유지하는 데 사용되는 메커니즘을 의미함
//                )
//                .csrf(csrf -> csrf.disable());  // CSRF 보호 비활성화 (필요한 경우)
//
//        return http.build();  // 필터 체인 빌드
//    }
//
//    //인증 관리자 관련 설정
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http,
//                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailsService userDetailsService) throws Exception {
//        // HttpSecurity에서 AuthenticationManagerBuilder 가져오기
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        // 사용자 정보 서비스와 패스워드 인코더 설정
//        authenticationManagerBuilder
//                .userDetailsService(userService) // UserDetailsService를 설정하여 사용자 정보를 로드
//                .passwordEncoder(bCryptPasswordEncoder); // 패스워드 인코더로 BCryptPasswordEncoder를 설정
//
//        // 설정이 완료된 AuthenticationManagerBuilder에서 AuthenticationManager 빌드 및 반환
//        return authenticationManagerBuilder.build();
//    }
//
//    //패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
