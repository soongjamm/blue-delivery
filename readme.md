# Blue Delivery

배달앱 서버 구현 프로젝트 

---
## 사용 기술

- Java11
- Spring boot
- Spring Data JPA
- MySQL
- Kafka
- Redis
- Gradle
- Docker

<br>

## 프로젝트 초기 설정
```shell 
cd bluedelivery-development-env

# Install packages & docker images, setup githooks
make init-env

# Migrate DB Schemas
make init-db

# Start Docker Containers
make docker-up
```


**githooks 적용 여부 확인**
```shell
# 다음 명령어가 '.githooks' 를 출력한다면 성공
git config --get core.hooksPath

# 적용되지 않았다면 프로젝트의 루트 경로에서 다음을 명령어를 실행
git config core.hooksPath
```
<br>

## Test & Lint 자동화
- 원격 저장소에 push 하기 위해서는 Test & Lint 를 통과해야 합니다.
- Test & Lint 는 원격 저장소에 push 하면 자동으로 실행됩니다.
<br>

**Lint 정보**  
checkstyle <a href="https://naver.github.io/hackday-conventions-java">네이버 캠퍼스 핵데이 Java 코딩 컨벤션</a>
- [indentation-tab] indent 를 tab -> space로 변경
- [no-trailing-spaces] 적용 안함
  

<br>

### Hexagonal Architecture 참고
```
├── order
│   ├── adapter
│   │   ├── in
│   │   │   └── web
│   │   │       └── PlaceOrderController.java
│   │   └── out
│   │       └── persistence
│   │           ├── OrderPersistenceAdapter.java
│   │           └── OrderRepositoryJpa.java
│   ├── application
│   │   ├── OrderMapper.java
│   │   ├── OrderValidator.java
│   │   ├── PlaceOrderService.java
│   │   └── port
│   │       ├── in
│   │       │   └── PlaceOrderUseCase.java
│   │       └── out
│   │           ├── LoadOrderPort.java
│   │           └── SaveOrderPort.java
│   ├── domain
│   │   ├── ExceptionMessage.java
│   │   ├── Order.java
│   │   ├── OrderCreatedEvent.java
│   │   ├── OrderDetails.java
│   │   └── OrderItem.java
│   ├── infra
│   │   └── OrderMessageRelay.java
│   └── interfaces
│       └── Cart.java

```

### Github Flow

Github Flow는 main 브랜치를 가 곧 product가 되는 전략입니다.

<img src="https://hackernoon.com/hn-images/1*iHPPa72N11sBI_JSDEGxEA.png" alt="img" style="zoom:50%;" />

(master = main) 

새로운 작업 전에 작업 내용을 담은 브랜치를 생성하고 작업합니다.

- feature-로그인구현
- readme-edit 등

작업이 끝나면 원격 브랜치로 push 하고, Pull Request를 통해 피드백을 받습니다. 

리뷰가 끝난 커밋은 main 브랜치로 병합됩니다.



## DB 형상관리
[flyway](https://flywaydb.org/documentation/usage/gradle/) 사용하여 런타임에 마이그레이션함.

- `@ActiveProfiles("test")`를 추가하면 테스트시에 h2 인메모리 db를 사용하여 테스트함
- `application-dev.yml`에 ${db_username} 등 placeholder는 시스템 환경변수에 자기의 db 이름, username 등에 맞춰 설정하면 됌

## docker compose 적용
[docker-compose](https://docs.docker.com/compose/)

- `docker compose up` 명령어 실행으로 필요한 이미지 다운로드 및 컨테이너 실행
