## Issue Convention

### [영문도메인명][영문구체적인기능명]

### 목표
- 사용자가 {어떤 행동}을 했을 때 {어떤 결과}를 얻는다.

---

### 백엔드 작업

#### 작업내용
- 
- 

---

### 프론트엔드 작업

#### 작업내용
- 
- 


## Commit Convention

### Types
- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `refactor` : 기능 변화 없는 코드 구조 개선
- `docs` : 문서 수정
- `test` : 테스트 코드 추가/수정
- `design` : CSS 등 UI/디자인 변경
- `chore` : 빌드/설정/기타 작업

### Scope
- 변경된 기능(가능하면 필수)
- 예: `user`, `cashflow`,  `transaction`

### Subject
- 변경 내용을 **간결하게** 작성
- 예: `캘린더 폰트 변경`

### Examples
- feat(login): jwt 인증 로직 추가
- fix(review): null pointer 예외 처리
- refactor(payment): 검증 로직 분리
- docs(readme): 커밋 컨벤션 문서화
- test(order): 주문 서비스 단위 테스트 추가
- design(transaction): 캘린더 폰트 변경
- chore(ci): github actions 워크플로 추가
