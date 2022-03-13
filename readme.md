# Blue Delivery

배달앱 서버 구현 프로젝트 

---
## 기술 스택
- [Flyway](https://flywaydb.org/documentation/usage/commandline) (DB 형상관리) 
- TBU

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

