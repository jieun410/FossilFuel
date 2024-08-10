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
      
