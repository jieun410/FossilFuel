package edu.example.springbootblog.global.jwt;


import io.jsonwebtoken.*;

import edu.example.springbootblog.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);

    }


    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE) //헤더 type: JWT //내용 iss: ajufresh@gmail.com(properties파일에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer()) //발행자(iss)를 설정
                .setIssuedAt(now) // 내용 iat :현재 시간
                .setExpiration(expiry) //내용 exp: expiry 멤버 변수값
                .setSubject(user.getEmail()) //토큰의 주체(sub)를 설정 내용 sub : 유저의 이메일
                .claim("email", user.getEmail())
                // 클래임을 해독해서 정보를 가져오기에, 클레임에 이메일을 꼭 넣어줘야 함
                .claim("id",user.getId()) //클레임 id : 유저 ID //성명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) //비밀키는 jwtProperties.getSecretKey()에서 가져옴
                .compact(); //토큰을 생성하여 문자열로 반환
    }

    //JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try{
            Jwts.parserBuilder() //토큰을 파싱하기 위한 JwtParser를 생성
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화
                    .build() // JwtParser 빌드
                    .parseClaimsJws(token); // 토큰 파싱 및 검증, 토큰을 파싱하여 서명과 클레임이 올바른지 확인

            return true;
        }catch(Exception e){
            return false; //복호화 과정에서 에러가 나면 유효하지 않은 토큰
        }
    }

    //토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims=getClaims(token); // 토큰에서 클레임을 가져옴
        //기본적으로 "ROLE_USER" 권한을 부여
        Set<SimpleGrantedAuthority> authorities= Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(),"",authorities),token,authorities);
        //Authentication 객체를 생성
        // 이 객체는 Spring Security에서 사용자 인증 정보를 저장하고 관리하는 데 사용됩니다.
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims=getClaims(token); //토큰에서 클레임을 가져옴
        return claims.get("id", Long.class); //클레임에서 ID 값을 Long 타입으로 추출
    }


    //JWT 토큰에서 클레임 정보를 가져오는 메서드
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()//클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)//토큰을 파싱하여 클레임(body)을 추출, 이 클레임에서 사용자 정보나 만료 시간 등의 정보를 얻을 수 있다.
                .getBody();

    }

    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtProperties.getSecretKey()).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
