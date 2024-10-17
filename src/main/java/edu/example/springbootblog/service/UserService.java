package edu.example.springbootblog.service;

import edu.example.springbootblog.domain.Role;
import edu.example.springbootblog.domain.User;
import edu.example.springbootblog.dto.securedto.AddUserRequest;
import edu.example.springbootblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // @Authoride + field : 를 통한 주입 방법 보다, 이런 생성자 주입 선호
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 가입 구현하기 : 회원 정보를 추가하는 서비스 메소드 구현
    public Long save(AddUserRequest SecureDTO) {
        return userRepository.save(User.builder()
                .email(SecureDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(SecureDTO.getPassword()))
                .role(Role.ROLE_USER)
                .build()).getId();
        // 패스워드를 저장할 때 시큐리티를 설정하며,
        // 패스워드 인코딩용으로 등록한 빈을 사용해서 암호화한 후에 저장한다.
    }
}
