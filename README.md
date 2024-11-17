### 작업 목록
- 기능 확인
- entity 정의
  - user
  - transfer quote
  - transfer request
- Util class 생성
  - [x] 암호화
  - [x] JWT
  - [x] ERROR

# 실행 방법

## 1. 환경 설정 (jdk 21) 및 실행
```shell
# jdk 21 설치
java --version

./gradlew test bootJar && java -jar build/libs/demo-moin-0.0.1-SNAPSHOT.jar
```

## 2. import postman file
- [postman file](./Moin.postman_collection.json)


# 기술 선택
- jdk 21
- spring boot 3.3.0
- webflux
- h2
- postman

# API
- [x] 회원가입 `POST {{API_URL}}/user/signup`
  - [x] 회원가입 시 이메일 중복 테스트
  - [x] 회원가입 시 비밀번호 암복호화 테스트
  - [x] 민감 정보 암호화 테스트
- [x] 로그인 `POST {{API_URL}}/user/login`
  - [x] 로그인 시 이메일 중복 테스트
  - [x] 로그인 시 이메일 형식 테스트
  - [x] 로그인 시 JWT 토큰 발급
  - [x] JWT 토큰 발급 유효시간 테스트
- [x] 송금 견적서 `POST {{API_URL}}/transfer/quote`
  - [환율 정보](https://crix-api-cdn.upbit.com/v1/forex/recent )
  - [x] 송금 견적서 요청 테스트
  - [x] 날짜 범위 조회 테스트
- [x] 송금 접수 요청 `POST {{API_URL}}/transfer/request`
  - [x] 송금 접수 성공 테스트
  - [x] 송금 만료 시 실패 테스트
  - [x] 송금 중복 실패 테스트
- [x] 회원의 거래 이력 `GET {{API_URL}}/transfer/list`
  - [x] 회원의 거래 이력 조회 필드 테스트

# 주요 컴포넌트
- `*Route` : API 라우팅
- `*Handler` : 비즈니스 로직
- `JwtSignComponent` : JWT 토큰 발급
- `JwtVerifer` : JWT 토큰 검증
- `MoinContentCrypto` : 암복호화 헬퍼 인터페이스
  - `MoinCryptoHelper` : 암복호화 헬퍼 (실제 암복호화를 호출하는 static helper)
- `MoinCrypto` : 암복호화 인터페이스
  - `MoinContentCryptoWrapper` : 암복호화 래퍼
  - `MoinCryptoImpl` : 암복호화 구현체
- `ErrorCode` : 응답 에러 코드
- `ExchangeInfoApi` : 환율 정보 API

# 회고
- 환율 조회 API 기능을 잘못 정의해서 기능 단위가 깔끔하지 못하게 되었습니다.
  - 일본은 일본만, 미국은 미국만 조회로 시작
- 환율 계산의 테스트 케이스가 부족합니다.
  - 다양한 금액들의 검증이 필요한 이유는 기능이 변경되면 개발자가 모두 인지하기 어렵기 때문에 테스트 코드로 검증이 이루어져야 함.
- 환율 목록 API를 먼저 보고, $1000 제한을 달러 기준으로 한다고 이해했지만, 구현하면서 엔화는 엔화만 가지고 견적과 금액 계산을 하도록 구현해서 추가 리팩터링이 필요합니다.
  - 환율 조회 API 명세가 잘못된 것과 연관.
  - 테이블 설계도 usdAmount, usdExchangeRate를 나중에 추가하면서 quote와 request 테이블 중복이 많습니다.
  - 리팩터링이 필요한 영역이라고 생각됩니다.
- 암복호화의 throw가 많아서 wrapper를 만들면서 사용성을 편리하도록 하려고 노력했습니다만, 이름이 정리되지 않은 느낌이 있습니다.
- 도메인이 더 풍성해지도록 설계가 되어야 하는데, 미흡한 것 같습니다.
- Exception handler가 제대로 동작하지 않는 케이스가 있지만, 다 해결하지 못했습니다.
- db에 연결된 테스트는 좋지 못하다고 생각하지만, `@Mock`과 `@InjectMocks`이 제대로 동작하지 db 기반으로 검증했습니다. (Mockito framework 이해도가 부족한 제 문제라고 생각합니다.)
- security authentication manager와 success/failure handler 사용이 미흡하다고 생각됩니다.
- request 조회나 request total amount validation을 Contextual을 사용해서 구현할 계획이었으나, 시간이 조금 부족해서 query로 구현했습니다. 

## 마지막으로..
개인정보 암호화는 이미 회사에 이미 다 있어서 암복호화에 대해 깊이가 부족했는데, 이번 과제로 많은 도움이 되었습니다. 감사합니다. (IV, XOR, AES, GCM, RSA 등의 대략적인 내용을 알게 되었습니다.)
keycloak 연동으로만 해봤던 jwt를 java 에서 조금 더 친해진 것 같습니다.
감사합니다.