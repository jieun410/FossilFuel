//package edu.example.springbootblog.jwt.controller;
//
//import edu.example.springbootblog.jwt.controller.dto.AccessTokenDto;
//import edu.example.springbootblog.jwt.service.ReissueService;
//import jakarta.servlet.http.HttpServletRequest;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/jwt")
//public class ReissueController {
//
//    private final ReissueService reissueService;
//
//    @PostMapping("/reissue")
//    public ResponseEntity<AccessTokenDto> reissueAccessToken(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        String refreshToken = header.substring(7);
//
//        AccessTokenDto accessTokenDto = reissueService.reissueAccessTokenByRefreshToken(refreshToken);
//
//        return ResponseEntity.ok(accessTokenDto);
//    }
//
//}