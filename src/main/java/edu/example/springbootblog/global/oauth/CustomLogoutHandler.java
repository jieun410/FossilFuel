package edu.example.springbootblog.global.oauth;

import edu.example.springbootblog.global.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component

public class CustomLogoutHandler implements LogoutHandler {
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    public CustomLogoutHandler(OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository) {
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
            System.out.println("Session invalidated.");
        }
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        System.out.println("Authorization request cookies removed.");
        if (authentication != null) {
            SecurityContextHolder.clearContext();
            System.out.println("Security context cleared.");
        }
    }
}
