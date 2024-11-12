# SpringBoot-> github-> AWS
- 깃허브 액션, 아마존 웹 서비스를 통해 CI/CD를 적용

## 기술스택
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Linux](https://img.shields.io/badge/Linux-0078D6?style=for-the-badge&logo=linux&logoColor=white)

## 아키텍처
<img width="700" alt="image" src="https://github.com/user-attachments/assets/72151a4b-28f5-451c-b369-0f47b6aef934">

## AWS 
- EC2 : 인스턴스 1, 엘라스틱ip 1
<img width="700" alt="image" src="https://github.com/user-attachments/assets/440bd876-3e94-4288-a586-c5eed40cd4cf">

- RDB : 무조건 EC2에 연결, SSD(gp2), 포트설정
<img width="700" alt="image" src="https://github.com/user-attachments/assets/fc2b7884-1655-44d6-ae8f-ac9ab9584887">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/cfcdf3c6-1703-474a-9c2c-7433dfff5a0c">

- 우분투리눅스 != 아마존리눅스
- sudo apt update || sudo dnf update
- sudo apt install openjdk-17-jdk || sudo dnf install java-17-amazon-corretto-devel
- sudo apt install git || sudo dnf install git
- $ git clone https://github.com/adorahelen/Solo-AWS
- $ cd Solo-AWS/
- $ chmod u+x gradlew
- $ ls -l gradlew
- $ ./gradlew build (시간 초과 및 에러)
- $ ./gradlew build -x test (테스트 없이 진행)
- $ java -jar SpringBootBlog-0.0.1-SNAPSHOT.jar

<img width="700" alt="image" src="https://github.com/user-attachments/assets/d74fbc3f-9eab-4016-82bc-d66479a198ea">

- 접속 -> 퍼블릭 IPv4 주소 + :8080 (스프링부트 기준)


## 직렬화 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/c9d3ea77-c37b-48a8-82c0-2cfd2d457dfa">

## 스프링시큐리티
<img width="700" alt="image" src="https://github.com/user-attachments/assets/6980dd67-50e8-423f-bf14-aac0890a83e8">

## 세션&토큰
<img width="700" alt="image" src="https://github.com/user-attachments/assets/8bb39dd5-ab01-4df4-b28f-57698e357931">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/b4c2a06b-892b-4232-9c72-608044e75f13">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/7d175432-c7b6-4674-a590-cd90c43af81c">





