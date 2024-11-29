package edu.example.springbootblog.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
//회원가입
public class AddUserRequest {
    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImage;



}
