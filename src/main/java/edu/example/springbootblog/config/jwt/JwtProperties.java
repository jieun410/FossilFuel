package edu.example.springbootblog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")// 자바 클래스에 프로피티값을 가져와서 사용하는 애너테이션
public class JwtProperties {
    private String issuer; // yml에 정의해둔, 키 발행자
    private String secretKey;
}
