# organizationAssignment

# 과제 설명
1. 아래와 같은 주소록 파일(CSV 형식)을 읽어 DB(MariaDB)에 저장
  - 직책은 팀장만 있음, 그 외 팀원
  - 동명이인인 경우 이름 + (B, C, D)로 표기
  - 순번, 이름, 내선번호, 휴대폰, 팀명, 직위, 직책
    e.g.
    1, 홍길동, 1000, 010-1234-5678, 웹개발1팀, 차장, 팀장
    2, 이순신, 2002, 010-2222-5678, 웹개발2팀, 과장,
    3, 유관순, 1001, 010-1111-5678, 웹개발1팀, 대리,
    4, 유관순, 1001, 010-1111-5678, 웹개발2팀, 사원,
2. 주소록 동기화하는 API 구현
3. 팀별 조직도를 트리로 표현하는 API 구현
  - 팀장은 상위, 팀(명)/팀원은 이름순 정렬
  - 팀명, 이름, 내선번호, 전화번호로 검색 (검색어 하나로 검색)

# 요구사항 추가
1. 파일
- 클래스패스가 아닌 곳에서 읽기
- 대용량 파일 처리
2. 파일 형식 변경 - 순번, 사번, 이름, (입사일), ...
3. 성/이름 구분
- 다국어(영어권) : 이름 + 성 표시 방법
4. 내선번호 중복 시
- 에러 처리 : 별도 테이블에 저장
- 동기화 API에 에러난 데이터 응답 추가 : 사번, 이름

# 개발 환경
- 자바 8
- IntelliJ
- Spirng boot (maven)
- JPA, MariaDB

# 기능 설계
## 데이터베이스
### 회원
- 모든 회원은 이름과 내선번호, 전화번호가 필수이다.
- 이름은 중복될 수 있으며, 중복 될 경우 이름 + 영어 대문자를 붙여야한다.
- 내선 번호, 휴대폰, 직위, 직책은 중복을 가질 수 있다.
- 회원은 각자 하나의 팀을 가지고 있다.
- 회원은 직위를 가지고 있다.
- 회원은 직책을 가지고 있지 않을 수 있다.
#### 회원 이름 중복에 대해서
- 같은 이름이 영어 대문자보다 많을 경우 어떻게 해야할 것인가? (예를 들어 A~Z)까지
다 썼을 경우
- 같은 이름을 가진 회원이 나가게 된다면 처리는 어떻게 할 것인가?
(홍길동B가 빠져나가고) 새롭게 홍길동 이름의 사원이 들어 올 경우 
  홍길동B의 이름을 다시 부여해도 괜찮은가?

### 팀
- 팀은 이름으로 구별할 수 있어야 한다. (하지만 고유 번호를 갖도록 만든다.)

### 직위
- ~~직위는 엔티티로서 관리하면서 확장성이 용이 할 수 있게 설계한다.~~
- 문자열로 처리한다.

### 직책
- ~~직책은 enum으로 설정한다.~~
- 문자열로 정하도록 한다.

## 주소록
- 주소록은 이름, 내선 번호, 휴대폰 번호가 있어야 할 것이다.
- 검색은 이름으로 검색을 할 수 있다.
- 주소록을 찾아와 수정할 수 있다. (전화번호 수정만이 가능)

## CSV 파일 읽기
- CSV 파일을 읽어 와 데이터베이스에 저장할 수 있어야 한다.