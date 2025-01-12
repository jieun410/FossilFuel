# 학과 동아리 웹 사이트 운영
- http://fossilfuel.site

## UI
<img width="900" alt="image" src="https://github.com/user-attachments/assets/b4333a32-2a17-4925-8986-09ebe5799dbb">
<img width="900" alt="image" src="https://github.com/user-attachments/assets/4482536f-b921-4abd-98d8-81bb9e8fb473">
<img width="900" alt="image" src="https://github.com/user-attachments/assets/58caedea-6a43-408d-97ef-531db2ba6408">


## 기술스택
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Linux](https://img.shields.io/badge/Linux-0078D6?style=for-the-badge&logo=linux&logoColor=white)

## 아키텍처
<img width="700" alt="image" src="https://github.com/user-attachments/assets/72151a4b-28f5-451c-b369-0f47b6aef934">


## AWS2 
- 아래에 있는 기존 방식을 진행하는 도중, pull 하지 않고 rm -rf로 기존의 것들을 삭제
  * 그리고 다시 git clone 을 통해 빌드하던 도중, ./gradlew build -x test 에서 무한로딩 발생
  * 인스턴스를 초기화 || 삭제하는 것 만이 해결책일꺼라고 생각
  * => 로컬 환경에서는 빌드가 정상적으로 이루어지는 것을 확인 
  * =>=> 도커를 통해 이미지를 배포하고, 이를 AWS 에 배포하는 것을 생각함 
- 01 ** Dockerfile 작성 **
- 02 $ ./gradlew clean build 
- 03 $ docker build -t fossilfuel-app . 
  * 도커 이미지 생성, 도커는 백그라운드에서 동작 중이어야 함 
  * 다 잘 됬는데, 계속 뻑나는 부분이 default.jpeg 를 못찾음 -> 코드 수정 (null)
  * 다른 방법 classpath, environment 시도 해도 도커는 계속 뻑남
- $ docker run -p 8080:8080 fossilfuel-app
  * ec2 용량 문제도 있는거 같아서, 초기화 했는데, 문제는 mac 이미지가 현재 사용 중인 아키텍처(linux/amd64)와 호환되지 않음
  * 일단 도커 허브 리포지토리 생성, 이미지 푸쉬는 된 상태
- docker tag fossilfuel-app6:latest kangminlog/fossilfuel:v1
- docker push kangminlog/fossilfuel:v1 

## AWS 
- EC2 : 인스턴스 1, 엘라스틱ip 1
<img width="700" alt="image" src="https://github.com/user-attachments/assets/440bd876-3e94-4288-a586-c5eed40cd4cf">

- EC2에 연결, SSD(gp2), 포트설정
<img width="700" alt="image" src="https://github.com/user-attachments/assets/fc2b7884-1655-44d6-ae8f-ac9ab9584887">
<img width="700" alt="image" src="https://github.com/user-attachments/assets/cfcdf3c6-1703-474a-9c2c-7433dfff5a0c">

- 우분투리눅스 != 아마존리눅스
- sudo apt update || sudo dnf update
- sudo apt install openjdk-17-jdk || sudo dnf install java-17-amazon-corretto-devel
- sudo apt install git || sudo dnf install git
- $ git clone https://github.com/adorahelen/FossilFuel
- $ cd FossilFuel/
  * vi application.yml + copy that
- $ chmod u+x gradlew
  * rm -rf build
  * ./gradlew clean
  * ./gradlew build -x test
- $ ./gradlew build -x test (테스트 없이 진행)
- $ nohup java -jar SpringBootBlog-0.0.1-SNAPSHOT.jar &
    * nohup: 터미널 세션을 닫아도 애플리케이션 계속 실행 (로그 nohup 저장)
    * &: 명령을 백그라운드에서 실행시켜 터미널을 계속 사용
- $ tail -f nohup.out (로그 실시간)


<img width="700" alt="image" src="https://github.com/user-attachments/assets/d74fbc3f-9eab-4016-82bc-d66479a198ea">

- 접속 -> 퍼블릭 IPv4 주소 + :8080 (스프링부트 기준)
- 가비아, 도메인 구매
- Route 53, 호스팅 생성
- 레코드 value, 가비아 등록
- 결과 = http://fossilfuel.site:8080

## URL 
- 퍼블릭 IPv4 주소, 퍼블릭 IPv4 DNS : 유동
    * 프라이빗 IPv4 주소 (?)
- 탄력적 주소 : 고정 (할당시 퍼블릭에 적용)
- http://fossilfuel.site:8080
    * 도메인구매(가비아), AWS Route53적용
    * 8080 삭제를 위한 여러 방법 존재(리버스 프록시: 엔진엑스,아파치)
    * firewalld를 통한 :8080 제거 진행
      
- $ sudo dnf install firewalld -y
- $ sudo systemctl start firewalld
- $ sudo systemctl enable firewalld
- $ sudo firewall-cmd --zone=public --add-forward-port=port=80:proto=tcp:toport=8080 --permanent
    * --zone=public: public 네트워크 영역에서 적용하도록 설정
    * --add-forward-port=port=80:proto=tcp:toport=8080: 포트 80에 들어오는 TCP 트래픽을 8080 포트로 리디렉션
    * --permanent: 설정을 영구적으로 적용하도록 지정
- $ sudo firewall-cmd --reload
- $ sudo firewall-cmd --list-all
- $ sudo netstat -tuln | grep 8080






## 도전과제
- RDS (현재는 H2, 서버재부팅시 방명록 초기화)
- https (회원 시스템 도입전에)
- CI&CD 파이프라인 구축 (아직은 수동)
