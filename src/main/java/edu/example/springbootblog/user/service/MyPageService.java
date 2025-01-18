package edu.example.springbootblog.user.service;

import edu.example.springbootblog.user.domain.User;
import edu.example.springbootblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {

        private final UserRepository userRepository;

        public User getUserByEmail(String email) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }


    @Transactional
    public void updateProfileImage(String email, MultipartFile profileImage ) {
        // Optional로 반환된 객체를 처리
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String profileUrl = null;
        byte[] profileImageBytes = null;

        if (profileImage != null) {
            try {
                profileImageBytes = profileImage.getBytes();
                String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
                profileUrl = fileName;

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to process the profile image", e);
            }
        }
        user.setProfileImage(profileImageBytes, profileUrl);
        userRepository.save(user);
    }
}

