
# 1. Base image 설정
FROM openjdk:17-jdk-slim

# 2. 작업 디렉터리 생성 및 이동
WORKDIR /app

# 3. 애플리케이션 JAR 파일을 컨테이너로 복사
COPY build/libs/SpringBootBlog-0.0.1-SNAPSHOT.jar app.jar

# 4. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]