# User/Auth 테스트 명세서

## 개요

| 항목 | 내용 |
|------|------|
| 테스트 대상 | User/Auth 기능 |
| 테스트 프레임워크 | JUnit 5, Mockito, MockMvc |
| 총 테스트 수 | 97개 |
| 테스트 레이어 | Service, Controller |

---

## 1. Service Layer 테스트

### 1.1 UserService 테스트

#### 회원가입 (signup)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 회원가입 성공 | 사용자 ID 반환 | - |
| 중복된 로그인 아이디 | CoreException | U003 |
| 중복된 이메일 | CoreException | U002 |
| 이메일 인증 미완료 | CoreException | A006 |

#### 프로필 수정 (updateProfile)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 닉네임 변경 성공 | 수정된 User 반환 | - |
| 로그인 아이디 변경 성공 | 수정된 User 반환, 토큰 삭제 | - |
| 이메일 변경 성공 | 수정된 User 반환 | - |
| 현재 로그인 아이디가 null | CoreException | C002 |
| 사용자를 찾을 수 없음 | CoreException | U001 |
| 변경할 항목 없음 | CoreException | C002 |
| 잘못된 비밀번호 | CoreException | U004 |
| 비밀번호가 null | CoreException | U004 |
| 중복된 로그인 아이디 | CoreException | U003 |
| 중복된 이메일 | CoreException | U002 |
| 이메일 인증 미완료 | CoreException | A006 |

#### 계정 삭제 (deleteAccount)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 계정 삭제 성공 | 정상 완료 | - |
| 현재 로그인 아이디가 null | CoreException | C002 |
| 사용자를 찾을 수 없음 | CoreException | U001 |

#### 프로필 조회 (getProfile)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 프로필 조회 성공 | User 반환 | - |
| 현재 로그인 아이디가 null | CoreException | C002 |
| 사용자를 찾을 수 없음 | CoreException | U001 |

---

### 1.2 AuthService 테스트

#### 로그인 (login)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 로그인 성공 | TokenResponse 반환 | - |
| 사용자를 찾을 수 없음 | CoreException | A003 |
| 비밀번호 불일치 | CoreException | A003 |

#### 토큰 갱신 (refreshToken)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 토큰 갱신 성공 | TokenResponse 반환 | - |
| 유효하지 않은 토큰 | CoreException | A001 |
| 만료된 토큰 | CoreException | A004 |
| 저장된 토큰과 불일치 | CoreException | A001 |
| 저장된 토큰이 없음 | CoreException | A001 |
| 사용자를 찾을 수 없음 | CoreException | U001 |

#### 로그아웃 (logout)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 로그아웃 성공 | 정상 완료 | - |
| 유효하지 않은 토큰 | CoreException | A001 |
| 만료된 토큰 | CoreException | A004 |

---

### 1.3 EmailVerificationService 테스트

#### 이메일 인증 요청 (requestVerification)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 인증 요청 성공 | 6자리 코드 발송 | - |
| 이메일이 null | CoreException | C002 |
| 이메일이 빈 문자열 | CoreException | C002 |

#### 이메일 인증 확인 (confirmVerification)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 인증 확인 성공 | 인증 완료 처리 | - |
| 이메일이 null | CoreException | C002 |
| 코드가 null | CoreException | C002 |
| 코드가 빈 문자열 | CoreException | C002 |
| 저장된 코드가 없음 | CoreException | A007 |
| 코드 불일치 | CoreException | A007 |

#### 인증 확인 및 소비 (assertVerifiedAndConsume)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 확인 및 소비 성공 | 인증 정보 삭제 | - |
| 이메일이 null | CoreException | C002 |
| 이메일 인증 미완료 | CoreException | A006 |

#### 이메일 인증 정보 삭제 (deleteByEmail)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 삭제 성공 | 정상 완료 | - |
| 이메일이 null | 무시 | - |
| 빈 문자열 | 무시 | - |

---

### 1.4 PasswordResetService 테스트

#### 비밀번호 재설정 요청 (requestReset)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 재설정 요청 성공 | 6자리 코드 발송 | - |
| 요청이 null | CoreException | C002 |
| loginId가 null | CoreException | C002 |
| email이 null | CoreException | C002 |
| 사용자를 찾을 수 없음 | CoreException | U001 |
| 이메일 불일치 | CoreException | C002 |

#### 비밀번호 재설정 확인 (confirmReset)
| 테스트 케이스 | 예상 결과 | ErrorCode |
|--------------|----------|-----------|
| 재설정 확인 성공 | 비밀번호 변경 | - |
| 요청이 null | CoreException | C002 |
| loginId가 null | CoreException | C002 |
| code가 null | CoreException | C002 |
| newPassword가 null | CoreException | C002 |
| 사용자를 찾을 수 없음 | CoreException | A005 |
| 저장된 코드가 없음 | CoreException | A005 |
| 코드 불일치 | CoreException | A005 |
| 기존 비밀번호와 동일 | CoreException | A008 |

---

## 2. Controller Layer 테스트

### 2.1 AuthController 테스트

#### POST /api/auth/signup
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 회원가입 성공 | 200 OK | - |
| 중복된 로그인 아이디 | 409 Conflict | U003 |
| 중복된 이메일 | 409 Conflict | U002 |
| 이메일 인증 미완료 | 400 Bad Request | A006 |
| 유효성 검증 실패 (짧은 loginId) | 400 Bad Request | C002 |
| 유효성 검증 실패 (잘못된 이메일) | 400 Bad Request | C002 |
| 유효성 검증 실패 (짧은 비밀번호) | 400 Bad Request | C002 |

#### POST /api/auth/login
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 로그인 성공 | 200 OK | - |
| 잘못된 자격 증명 | 401 Unauthorized | A003 |
| 유효성 검증 실패 (빈 loginId) | 400 Bad Request | C002 |

#### POST /api/auth/refresh
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 토큰 갱신 성공 | 200 OK | - |
| 인증되지 않음 | 401 Unauthorized | A001 |
| 세션 만료 | 401 Unauthorized | A004 |
| 사용자를 찾을 수 없음 | 404 Not Found | U001 |
| 유효성 검증 실패 (빈 refreshToken) | 400 Bad Request | C002 |

#### POST /api/auth/logout
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 로그아웃 성공 | 200 OK | - |
| 인증되지 않음 | 401 Unauthorized | A001 |
| 세션 만료 | 401 Unauthorized | A004 |

---

### 2.2 UserController 테스트

#### GET /api/users/me
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 프로필 조회 성공 | 200 OK | - |
| 사용자를 찾을 수 없음 | 404 Not Found | U001 |
| 잘못된 입력값 | 400 Bad Request | C002 |

#### PUT /api/users/update
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 닉네임 변경 성공 | 200 OK | - |
| 로그인 아이디 변경 성공 | 200 OK | - |
| 사용자를 찾을 수 없음 | 404 Not Found | U001 |
| 잘못된 비밀번호 | 400 Bad Request | U004 |
| 중복된 로그인 아이디 | 409 Conflict | U003 |
| 중복된 이메일 | 409 Conflict | U002 |
| 이메일 인증 미완료 | 400 Bad Request | A006 |
| 잘못된 입력값 | 400 Bad Request | C002 |
| 유효성 검증 실패 (짧은 loginId) | 400 Bad Request | C002 |
| 유효성 검증 실패 (빈 currentPassword) | 400 Bad Request | C002 |

#### DELETE /api/users/delete
| 테스트 케이스 | HTTP Status | ErrorCode |
|--------------|-------------|-----------|
| 계정 삭제 성공 | 200 OK | - |
| 사용자를 찾을 수 없음 | 404 Not Found | U001 |
| 잘못된 입력값 | 400 Bad Request | C002 |

---

## 3. ErrorCode 목록

| ErrorCode | HTTP Status | 메시지 |
|-----------|-------------|--------|
| C002 | 400 | 잘못된 입력값입니다. |
| U001 | 404 | 사용자를 찾을 수 없습니다. |
| U002 | 409 | 이미 사용 중인 이메일입니다. |
| U003 | 409 | 이미 사용 중인 아이디입니다. |
| U004 | 400 | 비밀번호가 올바르지 않습니다. |
| A001 | 401 | 인증이 필요합니다. |
| A003 | 401 | 아이디 또는 비밀번호가 올바르지 않습니다. |
| A004 | 401 | 세션이 만료되었습니다. |
| A005 | 400 | 비밀번호 재설정 코드가 유효하지 않거나 만료되었습니다. |
| A006 | 400 | 이메일 인증이 필요합니다. |
| A007 | 400 | 이메일 인증 코드가 유효하지 않거나 만료되었습니다. |
| A008 | 400 | 기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다. |

---

## 4. 테스트 실행 방법

```bash
# 전체 테스트 실행
./gradlew test

# User/Auth 관련 테스트만 실행
./gradlew test --tests "com.aespa.armageddon.core.domain.auth.service.*" \
               --tests "com.aespa.armageddon.core.api.auth.controller.*" \
               --tests "com.aespa.armageddon.core.api.user.controller.*"
```

---

## 5. 테스트 파일 위치

```
src/test/java/com/aespa/armageddon/
├── core/
│   ├── api/
│   │   ├── auth/controller/
│   │   │   └── AuthControllerTest.java
│   │   └── user/controller/
│   │       ├── UserControllerTest.java
│   │       └── MockUserDetailsArgumentResolver.java
│   └── domain/auth/service/
│       ├── UserServiceTest.java
│       ├── AuthServiceTest.java
│       ├── EmailVerificationServiceTest.java
│       └── PasswordResetServiceTest.java
```
