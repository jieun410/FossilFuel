package edu.example.springbootblog.config.jwt;

import edu.example.springbootblog.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service // 서비스를 Config에 작성?
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expireTime) {
        Date now = new Date();
        return CreateNewToken(new Date(now.getTime() + expireTime.toMillis()), user);
    }



    // " 생성 "JWT : Json Web Token 생성 메서드 정의 (만료일자, 유저 정보를 인자로 받아, 스트링[토큰]을 반환)
    private String CreateNewToken(Date expiryDate, User user) {
        Date timeofnow = new Date();


        return Jwts.builder() // 헤더 + 페이로드(데이터) + 싸인으로 구성된 생성 중 JWT

                // 1. 헤더 작업
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 헤더는 jwt, 내용은 yml에서 설정한 이메일 rlarkdals@gmail.com

                // 2. 페이로드 : 데이터 작업
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(timeofnow) // 발급일 명시
                .setExpiration(expiryDate) // 만료일 지정
                .setSubject(user.getEmail()) // 유저의 이메일을 포함
                .claim("id", user.getId()) // 토큰의 핵심인 claim id 는 유저 id 로 설정

                // 3. 서명 : 값 위조 방지를 위한
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
        // 해쉬 알고리즘 선택 부분이 헤더가 아니라 싸인에 명시되어 있는 점 주목


    } // 여기까지가 토큰을 생성하는 메서드

    // " 유효성 검증 " : 토큰 유효성 검증 (토큰 생성은 암호화 <-> 유효성 검증은 복호화)
    public boolean validToken(String token) { // 스트링으로 되어있는 토큰을 token 이라는 이름을 받는다.
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화 진행
                    .parseClaimsJws(token);

            return true;
        }catch (Exception e){ // 복호화 성공시 참을 반환하며, 복호화 과정에서 에러 발생시 유효하지 않은 토큰
            return false;
        }
    }// 토큰이 유효한지 검증하는 메서드, 프로퍼티즈에 선언한 비밀값과 함께 토큰 복호화를 진행한다. (암호화 한 상태에선 유효한지 알 수 없음)


    // 위에서 순차적으로 토큰 생성(암호화)->유효성 검증(복호화) 진행

    // 토큰을 받아, 인증 정보를 받은 객체 '어센티 케이션'을 반환한다. => 프로퍼티즈 파일에 저장한 비밀값으로 토큰을 복호화 하고 클레임을 가져오는
    // getClaims()를 호출하여 클레임 정보를 반환받아 사용자 이메일 들어있는 토큰제목 서브와
    // 토큰 기반으로 인증 정보를 가져오는 메서드
  public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰 넣고 돌려서 클레임 확득
      Set<SimpleGrantedAuthority> authorities
              = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

      return new UsernamePasswordAuthenticationToken
              (new org.springframework.security.core.userdetails.User(claims.getSubject()
              , "", authorities), token, authorities); // 이게 토큰 기반으로 인증 정보를 생성해서 반환하는 부분 ?
  }
//=======================================================================================

  // 토큰 기반으로 유저 아이디 가져옴
  public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class); // 아래에 있는 정의된 함수를 통해 클래임에서 아이디(롱으로)만 반환
  }


  private Claims getClaims(String token) { // 클레임 조회하고 클레임 반환하는 함수
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
  }
}
