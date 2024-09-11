//package edu.example.springbootblog.config.jwt;
//
//import edu.example.springbootblog.domain.User;
//import edu.example.springbootblog.repository.UserRepository;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.Duration;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class TokenProviderTest {
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtProperties jwtProperties;
//
//    // 1. 토큰을 생성하는 함수 테스트
//    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
//    @Test
//    void generateToken() {
//        // given
//        User testUser = userRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        // when
//        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
//
//        // then
//        Long userId = Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody()
//                .get("id", Long.class);
//
//        assertThat(userId).isEqualTo(testUser.getId());
//    }
//
//    // 2. 토큰 유효성 검증 테스트
//    @DisplayName("validToken() : 만료된 토큰인 경우, 탈취된 토큰인 경우 유효성 검증에 실패한다.")
//    @Test
//    void validToken_InvalidToken() {
//        // given
//        String token = JwtFactory.builder()
//                .expiration(new Date(new Date().getTime() - Duration.ofDays(7)
//                        .toMillis())).build().createToken(jwtProperties);
//
//        // when
//        boolean result = tokenProvider.validToken(token);
//
//        // then
//        assertThat(result).isFalse();
//    }
//    // 비 유효 토큰
//
//    @DisplayName("validToken() : 유효한 토큰인 때에 유효성 검증에 성공한다. ")
//    @Test
//    void validToken_ValidToken() {
//        //given
//        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
//
//        //when
//        boolean result = tokenProvider.validToken(token);
//    }
//
//
//}
