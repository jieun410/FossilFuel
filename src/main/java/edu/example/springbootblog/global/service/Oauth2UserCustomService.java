package edu.example.springbootblog.global.service;

import edu.example.springbootblog.user.domain.Role;
import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class Oauth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        savedOrUpdate(user);
        return user;
    }

    private User savedOrUpdate(OAuth2User oAuth2User) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        byte[] profileImageBytes = null;
        String profileUrl = null;

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email;
        String name;

        if (attributes.containsKey("kakao_account")) {
            email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
            System.out.println("Email from Kakao: " + email);
            name = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");

        } else {
            // 구글 사용자 정보 처리
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }

        String finalProfileUrl = profileUrl;
        byte[] finalProfileImageBytes = profileImageBytes;
        User user = userRepository.findByEmail(email)
                .map(entity -> {
                    // 로그 추가
                    System.out.println("User found, updating: " + entity);
                    return entity.update(name);
                })
                .orElseGet(() -> {
                    // 로그 추가
                    System.out.println("User not found, creating new user with email: " + email);



                    return User.builder()
                            .email(email)
                            .password(encoder.encode("123456"))
                            .nickname(name)
                            .profileImage(finalProfileImageBytes)
                            .profileUrl(finalProfileUrl)
                            .role(Role.ROLE_USER)
                            .build();
                });
        return userRepository.save(user); //새로운 사용자 정보를 저장하거나 업데이트된 사용자 정보를 저장한다.
    }
}