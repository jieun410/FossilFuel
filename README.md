# 스프링부트bolg프로젝트
# 결과
<img width="700" alt="image" src="https://github.com/user-attachments/assets/0c3ce4d7-994b-4c45-bbcd-5078497323f5">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/02f013c5-39ed-40bb-b2ef-78b481dc36ff">



## API 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/22138c52-ec8f-4102-a47a-fbd38ebf48d4">

- 클라이언트와 서버의 통신 매개체
- REST API : REST (Representational State Transfer), RESTful URL 설계
    * GET /users - 모든 사용자 조회
    * GET /users/123 - 특정 사용자 조회 (ID가 123인 사용자)
    * POST /users - 사용자 생성
    * PUT /users/123 - 사용자 업데이트
    * DELETE /users/123 - 사용자 삭제
- REST API는 기술이 아닌, URL 설계 방식 중 하나
    * 규칙 1. URL에는 동사를 쓰지말고, 자원을 표시한다. /students/1
    * 규칙 2. 동사를 쓸땐, HTTP 메서드를 사용한다. GET/articels/1, POST/articles
 
## 직렬화 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/c9d3ea77-c37b-48a8-82c0-2cfd2d457dfa">

## 에러
- OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
    * 무시해도 괜찮음 (test code 실행시 발견)
 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/ace4a2ad-67c5-4e63-a9ff-00dfda650e05">

<img width="700" alt="image" src="https://github.com/user-attachments/assets/2ad61d6c-4481-4dd6-86bf-bacff340137f">

- 리다이렉트를 시켜야하는데, RestController를 붙여놔서 뷰파일을 찾는게 아니라, JOSN 형식으로 스트링 데이터를 전송시킴 


## DTO 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/a036193e-3d73-4a9c-845b-41aab0b944ab">

## 테스트코드
    * 모키토는, 개발자가 동작을 직접적으로 제어할 수 있는 가짜 객체를 지원하는 테스트 프레임웍
    * Spring 어플리케이션은 여러 객체들 간의 의존성이 생기는데 이러한 의존성을 모키토를 이용하여 단절 시킴으로 단위 테스트를 쉽게 작성하는 것을 도와줌
- mockMvc는 Spring MockMvc 객체로, 실제 HTTP 요청을 모의하여 서버 측에서 이를 처리하는 방식을 테스트
- perform(delete(url, savedArticle.getId()))는 DELETE 요청을 해당 URL로 보냄
- 여기서 url은 "/api/articles/{id}"이며, {id} 부분은 savedArticle.getId()로 대체되어 특정 Article을 삭제하도록 요청
- andExpect(status().isOk())는 서버로부터 HTTP 200 OK 응답이 반환될 것을 기대하고, 이 응답은 삭제 요청이 성공적으로 처리되었음을 나타냄
    * 테스트 코드들은 추후에 컨트롤러나 다른 코드들이 변경되어도, 기존 코드가 잘 작동한다는 사실을 보증해 준다. (의존성 단절)

## 트랜잭션
- @Transactional은 스프링 프레임워크에서 제공하는 어노테이션, 메서드나 클래스에 적용하여 트랜잭션 관리를 자동으로 처리
    * 데이터베이스에서 일련의 작업이 모두 성공적으로 완료되거나, 중간에 하나라도 실패할 경우 전체 작업을 취소하는 단위
1. 트랜잭션 시작: 메서드가 호출되면 트랜잭션이 시작됩니다.
2. 커밋: 메서드가 정상적으로 종료되면 트랜잭션이 커밋됩니다. 즉, 모든 데이터 변경이 확정되고 데이터베이스에 반영됩니다.
3. 롤백: 메서드 실행 중 예외가 발생하면 트랜잭션이 롤백됩니다. 즉, 데이터베이스에 대한 모든 변경이 취소되고 이전 상태로 복구됩니다.
    * orElseThrow(() -> new IllegalArgumentException(“Not Found : “ + id)) : 트랜잭션 롤백, 변경사항 취소
    * 예외처리-롤백
    * <img width="700" alt="image" src="https://github.com/user-attachments/assets/2cea37a0-b673-486e-9215-d9ff61074af0">


## 템플릿엔진
- 스프링 서버에서 데이터를 받아, HTML에 그 데이터를 넣고 보여주는 도구
    * JSP, 타임리프, 프리마커
    * 템플릿 엔진으로 만들어진 결과물 -> 동적인 웹페이지
- 자바스크립트(백틱에러)
    * 2024-08-14T22:56:45.545+09:00  WARN 8055 --- [SpringBootBlog] [nio-8080-exec-7] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: "${id}"]
    * <img width="700" alt="image" src="https://github.com/user-attachments/assets/986bd28b-4d5f-4887-b019-167f57590746">
    * <img width="700" alt="image" src="https://github.com/user-attachments/assets/67ec3841-1783-464c-a7e5-44e86420331b">

## 스프링시큐리티
- 스프링 시큐리티는 스프링 하위 프레임워크이다.(보안을 담당하는)
- CRSF 공격 방어, 세션 고정 공격 방어, 요청 헤더 보안처리 등 다양한 보안 옵션 제공
    * CSRF attack : 사용자의 권한을 가지고 특정 동작을 수행하도록 유도하는 공격
    * 세션고정 공격 : 사용자의 인증 정보를 탈취하거나 변조하는 공격
- 핵심 : "필터를 기반으로 동작한다"
<img width="700" alt="image" src="https://github.com/user-attachments/assets/6980dd67-50e8-423f-bf14-aac0890a83e8">

- 스프링시큐리티컨피그 > 스프링시큐리티자동 > 컨트롤러(무의미?)

<img width="700" alt="image" src="https://github.com/user-attachments/assets/cdd65bce-defd-4fab-a2f4-df66300df196">

<img width="700" alt="image" src="https://github.com/user-attachments/assets/5a69d17c-ab88-4c83-88e4-10b2dc90bbea">



## 세션&토큰
- 세션 기반 인증
    * 일반적인 로그인 => 로그인 후 사용자는 즉각적인 효력 발휘, 원하는 작업 수행  
- 토큰 기반 인증
    * API 서버는 데이터를 제공하기 위해서만 존재
    * 사용자 인증 확인 방법 : 1. 서버 기반 인증(세션) || 2. 토큰 기반 인증 (스프링 시큐리티는 기본적으로 세션 기반 인증 제공)
    * 무상태성 : 사용자 인증 정보가 담겨 있는 토큰은 서버가 아닌 클라이언트에 있음 (세션은 서버 저장)
    * 확장성 : 서버를 확장할 때, 상태관리에 신경을 쓸 필요가 없다. ()
    * 무결성

<img width="700" alt="image" src="https://github.com/user-attachments/assets/360e5485-5399-4054-a732-2c58e9d84005">

- API 서버에서는 사용자의 상태를 유지하지 않기 때문에, 사용자 정보를 세션 등으로 유지하는 방식 대신, 토큰을 가지고 요청하는 방식으로 처리
- 즉 API 서버는 사용자의 어떠한 상태도 보관하지 않는다. (단지 전달된 토큰이 유효한지만 판단한다.)
- 토큰 : JWT (JSON - Web - Token) 문자열(별도의 형식으로 만들어진(암호 알고리즘이 적용된) )
    * JWT : 헤더-내용-서명으로 구성된다. (알고리즘-클레임(데이터)-서명(위조변조x))
    * 단점 : 탈취되는 문제 -> 내용(payload)에 만료시간을 지정하여 피해를 최소화
    * Access Token & Refresh Token : 



