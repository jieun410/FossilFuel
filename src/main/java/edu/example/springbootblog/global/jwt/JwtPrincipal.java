package edu.example.springbootblog.global.jwt;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

@Data
@RequiredArgsConstructor
public class JwtPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 1L; // serialVersionUID 추가

    private final String username;


    @Override
    public String getName() {
        return username;
    }

    @Override
    public String toString() {
        return "JwtPrincipal{" +
                "username='" + username + '\'' +
                '}';
    }

}
// 테스트2