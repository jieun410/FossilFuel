package edu.example.springbootblog.user.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor

@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,updatable = false)
    private Long id;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //OAuth관련키 저장
    @Column(name="nickname",unique = true)
    private String nickname;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;  // 이미지 자체 저장

    @Column(name = "profile_url")
    private String profileUrl;  // 이미지 URL 저장



    @Builder
    public User(String email, String password, String nickname, byte[] profileImage, String profileUrl, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.profileUrl = profileUrl;  // URL 저장(이미지 구별, 호출)
        this.role = role;
    }

    // 프로필 이미지를 Base64 문자열로 변환
    public String getProfileImageAsBase64() {
        if (profileImage != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(profileImage);
        }
        return null;
    }


    // 프로필 이미지 변경 메소드 추가
    public void setProfileImage(byte[] profileImage, String profileUrl) {
        this.profileImage = profileImage;
        this.profileUrl = profileUrl;
    }

    //사용자 이름 변경
    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }


    @Transactional
    public User updatePW(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        System.out.println(password + " 4. 제발 되라");
        this.password = password;
        return this;
    }


    @Override //권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user")); //사용자 이외의 권한이 없기 때문에 user권한만 담아 반환
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        //만료되었는지 확인하는 로직
        return true; //true=>만료되지 않았음
    }

    //계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        //계정 잠금되었는지 확인하는 로직
        return true; //true=>잠금되지 않았음
    }

    //패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        //패스워드가 만료되었는지 확인하는 로직
        return true;//ture=>만료되지 않았음
    }

    //계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        //계정이 사용 가능한지 확인하는 로직
        return true; //true=> 사용가능
    }


}
