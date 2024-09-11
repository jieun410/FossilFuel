//package edu.example.springbootblog.config.jwt;
//
//import io.jsonwebtoken.Header;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.time.Duration;
//import java.util.Date;
//import java.util.Map;
//
//import static java.util.Collections.emptyMap;
//
//@Getter
//public class JwtFactory {
//    private String subject = "test@email.com";
//    private Date issuedAt = new Date();
//    private Date expiration
//            = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
//    private Map<String, Object> claims = emptyMap();
//
//    @Builder
//    public JwtFactory(String subject,
//                      Date issuedAt,
//                      Date expiration,
//                      Map<String, Object> claims) {
//        this.subject = subject != null ? subject : this.subject;
//        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
//        this.expiration = expiration != null ? expiration : this.expiration;
//        this.claims = claims != null ? claims : this.claims;
//    }
//
//    public static JwtFactory withDefaultValues() {
//        return JwtFactory.builder().build();
//    }
//
//    // jjwt 의존성을 통해 추가한 라이브러리를 사용해서 JWT 토큰을 생성한다.
//    public String createToken(JwtProperties jwtProperties) {
//        return Jwts.builder()
//                .setSubject(subject)
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//                .setIssuer(jwtProperties.getIssuer())
//                .setIssuedAt(issuedAt)
//                .setExpiration(expiration)
//                .addClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
//                .compact();
//    }
//    // 빌드 패턴을 사용해 객체를 만들 때 테스트가 필요한 데이터만 선택한다.
//    // 빌더 패턴을 사용하지 않으면, 기본 값을 사용한다.
//}
