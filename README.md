# 학과 동아리 웹 사이트 운영
- http://fossilfuel.site
- https://fossilfuel.site

## UI
<img width="900" alt="image" src="https://github.com/user-attachments/assets/b4333a32-2a17-4925-8986-09ebe5799dbb">
<img width="900" alt="image" src="https://github.com/user-attachments/assets/4482536f-b921-4abd-98d8-81bb9e8fb473">
<img width="900" alt="image" src="https://github.com/user-attachments/assets/ce03855f-b1b3-473d-86cd-687c489ec9a5" />
<img width="900" alt="image" src="https://github.com/user-attachments/assets/58caedea-6a43-408d-97ef-531db2ba6408">
<img width="900" alt="image" src="https://github.com/user-attachments/assets/970b457b-5108-49cd-962f-a7468e6e33f8" />
<img width="900" alt="image" src="https://github.com/user-attachments/assets/f9c2f58d-2157-4f47-8d95-4a491fad4fd3" />
<img width="900" alt="image" src="https://github.com/user-attachments/assets/82acec3b-bc2e-4647-ae1d-00f0934ab9fe" />
<img width="900" alt="image" src="https://github.com/user-attachments/assets/be701055-0ce8-42bd-b960-571b9e2676c7" />






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
  * rm -rf build (아래 명령어가 동일한 작업 진행, =불필요)
  * ./gradlew clean
  * ./gradlew build -x test
- $ ./gradlew build -x test (테스트 없이 진행)
- $ nohup java -jar SpringBootBlog-0.0.1-SNAPSHOT.jar &
    * nohup: 터미널 세션을 닫아도 애플리케이션 계속 실행 (로그 nohup 저장)
    * &: 명령을 백그라운드에서 실행시켜 터미널을 계속 사용
- $ tail -f nohup.out (로그 실시간)
- ** 무슨 작업전에 꼭 top으로 메모리 사용량 확인할 것 **

- [변경 사항 적용법 pull->clean->build]
  * cd FossilFuel
  * git pull origin main
  * ./gradlew clean
  * ./gradlew build -x test


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


- CI&CD 파이프라인 구축 (아직은 수동) -> stop git action
  * git pull - clean - build

### = 해결 =

- RDS 트러블 슈팅
  * 추가요금 발생 -> 그냥 비용 지불 (ip4 과금은 이미 시작됨)
  * 01 퍼블릭 접근 여부 (RDS) -> 지금 끄고, 자고 일어나서 리부팅시 날아감 ->필수
  * 02 디비 백업 설정 여부 (RDS) -> 아마 필수(01 + 03 => 인텔리제이 날아갔었음)
  * 03 * .yml 파일만 업데이트 하고 "build"를 다시 하지 않음
  * ** 즉 기존에 H2db 베이스 .jar 를 계속 돌리고 있던게 원인
  * ** 로컬에서는 상관없지만, 리눅스 서버 위에서는 RUN  = 리빌드는 필수
  * $ ps -ef | grep [여기는 .jar 파일 이름]
  * $ kill -9 [여기는 ps id] 


- DNS 트러블 슈팅  
  * https 적용하려 ssl/tls 인증서 발급 -> 로드밸런스 생성 -> 레코드 삭제(miss) : dns 접속 뻑남
  * => fossilfuel.site의 A 레코드가 삭제된 상태
  * 도메인이 IP 주소로 매핑되지 않아 도메인으로 접속이 불가능한 상태 -> A 레코드를 다시 설정
  * 레코드 유형(Type): A
  * 이름(Name): 빈칸(또는 fossilfuel.site 입력)
  * 저장 후 몇 분에서 몇 시간이 지나면 DNS가 전파되어 http://fossilfuel.site/로 접속이 가능

 <img width="700" alt="image" src="https://github.com/user-attachments/assets/cd3fd5fe-b698-4022-9d1c-5d5b2e59c680" />
 <img width="700" alt="image" src="https://github.com/user-attachments/assets/0ea4641b-fb77-4c20-a8e7-f9fea47d01b2" />

- https 적용 (24.11.13 ~ 25.01.13) 
  * SSL/TLS 인증서 준비 : AWS Certificate Manager(ACM)에서 인증서 요청
  * AWS Elastic Load Balancer와 Target Group 구성
  * HTTPS 리스너 추가
  * HTTP → HTTPS 리다이렉트 설정
  * * 보안그룹에 https 인바운드 규칙이 없었던게 무한로딩 원인
 
 <img width="344" alt="image" src="https://github.com/user-attachments/assets/9cffd05a-adb3-4e5a-a957-f58b1a21a594" />


## 전체 구조 요약

1.	로드 밸런서
•	fossilfuel-load는 외부에서 들어오는 HTTP/HTTPS 요청을 처리하고, Target Group으로 트래픽을 라우팅합니다.
2.	Target Group
•	fossilfuel Target Group은 등록된 EC2 인스턴스로 트래픽을 전달합니다.
3.	리스너 및 규칙
•	HTTP(80) 요청은 HTTPS(443)로 리디렉션됩니다.
•	HTTPS 요청은 Target Group으로 전달됩니다.

      
