package edu.example.springbootblog.user.service;

import edu.example.springbootblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//스프링 시큐리티에서 사용자 인증 시 사용자를 이메일을 통해 조회하여, 인증 처리에 필요한 사용자 정보를 제공
public class UserDetailService implements UserDetailsService { //스프링 시큐리티에서 사용자의 정보를 가져오는 UserDetailsService인터페이스 구현
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)  {
        // email을 기준으로 사용자 정보를 조회하여 반환
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email)); //메서드나 생성자가 예상하지 못한 인자를 받았을 때 발생하는 예외
    }
}
