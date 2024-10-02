package edu.example.springbootblog.jwt.config;

import edu.example.springbootblog.dto.UserDTO;
import edu.example.springbootblog.jwt.controller.dto.JwtInfoDto;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;

//    private final Long accessTokenExpireTime;
//    private final Long refreshTokenExpireTime;

    @Getter  // Lombok을 사용해 getter 자동 생성
    private final Long accessTokenExpireTime;

    @Getter  // refreshTokenExpireTime에도 적용 가능
    private final Long refreshTokenExpireTime;


    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    // application.properties 에 저장된 키 및 시간
    //      * Caused by: io.jsonwebtoken.security.WeakKeyException: 키 값이 너무 짧습니다.
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access_expiration_time}") Long accessTokenExpireTime,
                   @Value("${jwt.refresh_expiration_time}") Long refreshTokenExpireTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;

        logger.info("JWT 유틸리티 초기화 완료: accessTokenExpireTime={}, refreshTokenExpireTime={}",
                accessTokenExpireTime, refreshTokenExpireTime);

    } // 생성자
    // 설정파일에서 바인딩해놓은 secret와 access_expiration_time, refresh_expiration_time을 사용

    /**
     * accessToken, refreshToken을 생성한다.
     * 컴포넌트 내의 createAccessToken, createRefreshToken을 호출하여 생성한다.
     * @param userInfoDto
     * @return JwtInfoDto
     */
    // 1. 엑세스 토큰&리프레쉬 토큰 : 발행 (토큰 서비스에서 호출)
    public JwtInfoDto createToken(UserDTO userInfoDto) {

        // 위에는 설정값을 불러와서 지정, 여기서 시간 삽입
        Date accessTokenExpirationTime = new Date(currentTimeMillis() + accessTokenExpireTime);
        Date refreshTokenExpirationTime = new Date(currentTimeMillis() + refreshTokenExpireTime);

        String accessToken = createAccessToken(userInfoDto, accessTokenExpirationTime);
        String refreshToken = createRefreshToken(userInfoDto, refreshTokenExpirationTime);

        logger.info("토큰 생성 완료: 액세스 토큰 만료 시간은 {}입니다.", accessTokenExpirationTime);
        return JwtInfoDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpirationTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpirationTime)
                .build();
    }

    /**
     * accessToken 생성
     * @param userInfoDto
     * @param expirationTime
     * @return accessToken
     */

    // 1-1 엑세스 토큰 발행 : private -> public, use ReissueService
    public String createAccessToken(UserDTO userInfoDto, Date expirationTime) {
        logger.debug("액세스 토큰 생성 중: 사용자 정보 - 이메일: {}", userInfoDto.getEmail() );
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("AccessToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("UserId", userInfoDto.getId())
                .claim("email", userInfoDto.getEmail())
                //.claim("role", userInfoDto.getRole())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refreshToken 생성
     * @param userInfoDto
     * @param expirationTime
     * @return
     */

    // 1-2 리프레쉬 토큰 발행 :
    private String createRefreshToken(UserDTO userInfoDto, Date expirationTime) {
        logger.debug("리프레시 토큰 생성: 사용자 이메일 = {}", userInfoDto.getEmail());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("RefreshToken")
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim("UserId", userInfoDto.getId())
                .claim("email", userInfoDto.getEmail())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * token을 파싱하여 email을 리턴
     * @param token
     * @return email
     */

    // 2. 토큰에서 이메일을 추출(필터에서 사용)
    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    /**
     * 해당 token이 유효한지 체크
     * @param token
     * @return
     */

    // 3. 토큰 유효성 검증(필터에서 사용)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("JWT 토큰이 만료되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰 입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims가 비어있습니다.", e);
        }
        return false;
    }


    // 2-1 (토큰, 이메일 추출시 사용)
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            logger.debug("토큰 만료됨, 만료된 토큰의 claims 반환");

            return e.getClaims();
        }
    }

    // 4.
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        logger.debug("토큰에서 사용자 ID 추출: {}", claims.get("UserId"));

        return claims.get("UserId", Long.class);
    }


    // 5.
    public Long getAccessTokenExpireTime() {
        return this.accessTokenExpireTime;
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseClaims(token); // 토큰에서 클레임을 추출
        Date expiration = claims.getExpiration(); // 클레임에서 만료 시간 추출
        boolean isExpired = expiration.before(new Date());
        logger.debug("토큰 만료 여부 확인: {}", isExpired);

        return isExpired; // 현재 시간과 비교하여 만료 여부 반환
    }
}
