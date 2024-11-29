package edu.example.springbootblog.user.dto;

import lombok.Data;

@Data
public class PwFindRequest {
    private String nickname;
    private String email;
    private String password;
}
