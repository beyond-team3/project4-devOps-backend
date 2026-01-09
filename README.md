## Commit Convention

### 기본 형식
type(scope): subject

### Types
- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `refactor` : 기능 변화 없는 코드 구조 개선
- `docs` : 문서 수정
- `test` : 테스트 코드 추가/수정
- `design` : CSS 등 UI/디자인 변경
- `chore` : 빌드/설정/기타 작업

### Scope
- 변경된 **기능 또는 모듈명**

#### 예시
- user
- cashflow
- transaction
- goal

### Subject
- 변경 내용을 **간결하고 명확하게** 작성
- 마침표 사용 ❌

### Commit 예시
- feat(login): jwt 인증 로직 추가
- fix(review): null pointer 예외 처리
- refactor(payment): 검증 로직 분리
- docs(readme): 커밋 컨벤션 문서화
- test(order): 주문 서비스 단위 테스트 추가
- design(transaction): 캘린더 폰트 변경
- chore(ci): github actions 워크플로 추가
