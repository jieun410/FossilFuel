package edu.example.springbootblog.repository;

import edu.example.springbootblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // 스프링 데이터 JPA는 메서드 규칙에 맞춰, 메서드 선언시 -> 이름을 분석해 자동으로 쿼리를 생성함;
    // 아마 자동으로 생성할 쿼리
    // FORM users WHERE email = #{email}

}
