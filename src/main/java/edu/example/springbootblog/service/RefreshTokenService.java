//package edu.example.springbootblog.service;
//
//import edu.example.springbootblog.domain.RefreshToken;
//import edu.example.springbootblog.repository.RefreshTokenRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class RefreshTokenService {
//    private final RefreshTokenRepository refreshTokenRepository;
//    public RefreshToken findByToken(String token) {
//        return refreshTokenRepository.findByRefreshToken(token)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
//    }
//
//}
