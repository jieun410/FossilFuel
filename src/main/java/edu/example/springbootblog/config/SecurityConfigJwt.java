package edu.example.springbootblog.config;


import edu.example.springbootblog.jwt.config.JwtAuthenticationFilter2;
import edu.example.springbootblog.jwt.config.JwtUtil;
import edu.example.springbootblog.jwt.handler.CustomAccessDeniedHandler;
import edu.example.springbootblog.jwt.handler.CustomAuthenticationEntryPoint;
import edu.example.springbootblog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfigJwt {

    private final UserDetailService userDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/", "/login", "/signup",
            "/api/v1/auth/**", "/swagger-ui/index.html#/", "/api/v1/jwt/reissue",
            "/"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()

                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF, CORS
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        //세션 관리 상태 없음으로 구성
        http
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 핸들링 정의 및 구성
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        // JwtAuthFilter를 filterChain에 추가 (모든 요청 가로채고, 인증 객체 생성 후 시큐리티 홀더에 저장 여기서)
        http
                .addFilterBefore(jwtAuthenticationFilter2(), UsernamePasswordAuthenticationFilter.class);

        // permit, authenticated 경로 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll() // 지정한 경로는 인증 없이 접근 허용
                        .requestMatchers("/", "/api/login","/login", "/js/**", "/css/**", "/img/**").permitAll()
                        //.anyRequest().authenticated()); // 나머지 모든 경로는 인증 필요
                        .anyRequest().permitAll());


        return http.build();
    }

    // 커스텀(토큰) 필터 빈으로 등록
    @Bean
    public JwtAuthenticationFilter2 jwtAuthenticationFilter2() {
        return new JwtAuthenticationFilter2(userDetailsService, jwtUtil);
    }

        @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}