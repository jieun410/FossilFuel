package edu.example.springbootblog.global.repository;

import edu.example.springbootblog.global.oauth.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

public class OAuth2AuthorizationRequestBasedOnCookieRepository   implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"; //쿠키에 저장할 때 사용되는 쿠키의 이름
    private final static int COOKIE_EXPIRE_SECONDS = 18000; //쿠키의 만료 시간을 정의 18,000초(약 5시간) 동안 쿠키가 유효하다.

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    //요청(Request) 객체로부터 쿠키를 읽어와 저장된 OAuth2 인증 요청(OAuth2AuthorizationRequest)을 로드하는 메서드
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME); // 특정 이름의 쿠키를 요청에서 가져오는 도구
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class); //쿠키에서 읽어온 값을 OAuth2AuthorizationRequest 객체로 역직렬화(deserialize)
    }

    // OAuth2 인증 요청을 쿠키에 저장하는 메서드
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        //authorizationRequest가 null인 경우, 이미 저장된 쿠키를 삭제하는 메서드 removeAuthorizationRequestCookies를 호출한다.
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        //그렇지 않으면, CookieUtil.serialize 메서드를 사용하여 인증 요청을 직렬화(serialize)하고, 그 값을 쿠키로 저장한다.
        // 쿠키의 만료 시간은 5시간(COOKIE_EXPIRE_SECONDS)
        //CookieUtil.addCookie: 쿠키를 응답(Response)에 추가하는 유틸리티 메서드
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    //특정 요청(Request)에서 인증 요청 쿠키를 삭제하는 메서드
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }

}
//전반적인 동작 요약:
//저장 (saveAuthorizationRequest):OAuth2 인증 과정에서 OAuth2AuthorizationRequest 객체가 생성되면, 이 요청 정보를 직렬화하여 쿠키로 저장한다.
//불러오기 (loadAuthorizationRequest):
//OAuth2 인증 진행 중, 저장된 인증 요청이 필요한 경우 요청으로부터 쿠키를 읽어와 인증 요청 객체를 역직렬화하여 반환합니다.
//삭제 (removeAuthorizationRequest 및 removeAuthorizationRequestCookies):
//OAuth2 인증 요청이 완료되거나 실패한 후, 해당 인증 요청 정보를 담고 있던 쿠키를 삭제합니다.