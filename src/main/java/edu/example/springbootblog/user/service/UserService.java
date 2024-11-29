package edu.example.springbootblog.user.service;

import edu.example.springbootblog.user.domain.Role;
import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.dto.AddUserRequest;
import edu.example.springbootblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository; // 사용자 정보를 처리하는 레포지토리



    // 사용자 저장 메서드 (회원가입)
    public Long save(AddUserRequest dto) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        byte[] profileImageBytes = null;
        String profileUrl = null;


        if (!dto.getProfileImage().isEmpty()) {
            try {
                profileImageBytes = dto.getProfileImage().getBytes();

                String fileName = UUID.randomUUID() + "_" + dto.getProfileImage().getOriginalFilename();
                profileUrl = fileName;

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to process the profile image", e);
            }
        } else {
            try {
                File defaultImage = new File("src/main/resources/static/img/default.jpeg");
                profileImageBytes = Files.readAllBytes(defaultImage.toPath());
                String fileName = UUID.randomUUID() + "_default.jpeg" ;
                profileUrl = fileName;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load the default profile image", e);
            }
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .profileImage(profileImageBytes)
                .profileUrl(profileUrl)
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user).getId();
    }

    // ID로 사용자 조회
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    // 이메일로 사용자 조회
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));
    }

    //사용자 탈퇴
    @Transactional
    public void deleteUserByUsername(String username) {
        User user=userRepository.findByEmail(username)
                        .orElseThrow(()->new IllegalArgumentException("No user found with email: " + username));

        // 사용자 이메일로 작성된 게시글과 댓글의 작성자 필드를 "탈퇴한 사용자입니다."로 변경
        userRepository.delete(user);
    }


    // 특정 사용자의 닉네임을 null로 설정하는 메서드 (이메일로 사용자 조회)
    @Transactional // 데이터 변경 시 트랜잭션을 보장함
    public void setNicknameNullByEmail(String email) {
        log.info("닉네임을 null로 변경할 이메일: " + email);
        userRepository.findByEmail(email).ifPresent(user -> {
            // 닉네임을 null로 변경 후 저장
            user.update(null);
            userRepository.save(user);
            log.info("닉네임이 null로 변경됨: " + user);
        });
    }

    //사용자가 좋아요 누른 게시글 조회
    //사용자가 쓴 게시글 조회
    //시용자가 쓴 댓글 조회

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new IllegalArgumentException("No user found with email: " + username));
    }
}
//전체 동작 흐름
//사용자 가입 요청이 들어오면, AddUserRequest DTO를 통해 사용자 정보가 전달됩니다.
//save 메서드가 호출되면, 비밀번호는 암호화되고, 이메일과 암호화된 비밀번호를 가진 User 객체가 생성됩니다.
//이 User 객체는 UserRepository를 통해 데이터베이스에 저장됩니다.
//저장된 User 객체의 ID가 반환되어, 사용자 가입 작업이 완료됩니다.