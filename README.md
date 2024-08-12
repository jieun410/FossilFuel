# 스프링부트 bolg 프로젝트

## API 
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

## DTO 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/a036193e-3d73-4a9c-845b-41aab0b944ab">

## 테스트 코드는 언제 왜 작성할까?
    * 모키토는, 개발자가 동작을 직접적으로 제어할 수 있는 가짜 객체를 지원하는 테스트 프레임웍
    * Spring 어플리케이션은 여러 객체들 간의 의존성이 생기는데 이러한 의존성을 모키토를 이용하여 단절 시킴으로 단위 테스트를 쉽게 작성하는 것을 도와줌
- mockMvc는 Spring MockMvc 객체로, 실제 HTTP 요청을 모의하여 서버 측에서 이를 처리하는 방식을 테스트
- perform(delete(url, savedArticle.getId()))는 DELETE 요청을 해당 URL로 보냄
- 여기서 url은 "/api/articles/{id}"이며, {id} 부분은 savedArticle.getId()로 대체되어 특정 Article을 삭제하도록 요청
- andExpect(status().isOk())는 서버로부터 HTTP 200 OK 응답이 반환될 것을 기대하고, 이 응답은 삭제 요청이 성공적으로 처리되었음을 나타냄

## 트랜잭션 처리 
- @Transactional은 스프링 프레임워크에서 제공하는 어노테이션, 메서드나 클래스에 적용하여 트랜잭션 관리를 자동으로 처리
    * 데이터베이스에서 일련의 작업이 모두 성공적으로 완료되거나, 중간에 하나라도 실패할 경우 전체 작업을 취소하는 단위
1. 트랜잭션 시작: 메서드가 호출되면 트랜잭션이 시작됩니다.
2. 커밋: 메서드가 정상적으로 종료되면 트랜잭션이 커밋됩니다. 즉, 모든 데이터 변경이 확정되고 데이터베이스에 반영됩니다.
3. 롤백: 메서드 실행 중 예외가 발생하면 트랜잭션이 롤백됩니다. 즉, 데이터베이스에 대한 모든 변경이 취소되고 이전 상태로 복구됩니다.
    * orElseThrow(() -> new IllegalArgumentException(“Not Found : “ + id)) : 트랜잭션 롤백, 변경사항 취소

