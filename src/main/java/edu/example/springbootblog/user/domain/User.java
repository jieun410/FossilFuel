package edu.example.springbootblog.user.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 사용자 정보를 관리하는 엔티티 클래스
@Entity
@Table(name = "users")
public class User {

    // 사용자 ID (자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 사용자 이름 (필수, 최대 길이 100자)
    @Column(nullable = false, length = 100)
    private String username;

    // 이메일 주소 (필수, 고유)
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    // 비밀번호 (필수)
    @Column(nullable = false)
    private String password;

    // 전화번호 (선택, 최대 길이 15자)
    @Column(length = 15)
    private String phoneNumber;

    // 사용자 역할 (예: ADMIN, USER, 열거형 사용)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    // 계정 활성 상태 (기본값: true)
    @Column(nullable = false)
    private boolean isActive = true;

    // 계정 생성일
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 계정 수정일
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Getters 및 Setters

    // 엔티티가 persist 되기 전에 생성일과 수정일을 설정하는 메서드
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 엔티티가 업데이트 되기 전에 수정일을 설정하는 메서드
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

// 사용자 역할을 정의하는 열거형
enum Role {
    ADMIN, // 관리자
    USER   // 일반 사용자
}