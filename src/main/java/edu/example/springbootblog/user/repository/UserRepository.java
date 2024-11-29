package edu.example.springbootblog.user.repository;


import edu.example.springbootblog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //email로 사용자 정보를 가져옴

    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmailAndNickname(String email, String nickname);

    @Modifying
    @Query("UPDATE User u SET u.profileImage = :profileImage WHERE u.email = :email")
    void updateProfileImage(@Param("email") String email, @Param("profileImage") byte[] profileImage);



   // void deleteByUsername(String email);
}
