package edu.example.springbootblog.user.service;

import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserDataService {

    private static final Logger log = LoggerFactory.getLogger(FindUserDataService.class);

    private final UserRepository userRepository;

    // 닉네임으로 이메일 찾기
    public User findEmailByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + nickname));

    }

    // 이메일과 닉네임으로 비밀번호 찾기 (복호화 포함)
    public User findPasswordByEmailAndNickname(String email, String nickname) {

        Optional<User> user = userRepository.findByEmailAndNickname(email, nickname);
        log.info("2 : " + user.toString());

        return user.orElse(null);
    }

    @Transactional
    public void updatePasswordByEmailAndNickname(String email, String nickname, String password) {
        User user = userRepository.findByEmailAndNickname(email, nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.updatePW(encoder.encode(password));

    }
} // BCryptPassword Encoder 는 단방향 암호화 방식을 사용하기 때문에 복호화가 불가능(일치 여부는 확인 가능)




