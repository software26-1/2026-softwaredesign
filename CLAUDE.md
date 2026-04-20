# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요
중/고등학교 교사용 학생 성적, 학생부, 피드백, 상담 관리 시스템

## 개발 환경 설정

### 필수 인프라 실행 (Docker)
```bash
cd backend
docker-compose up -d   # PostgreSQL 16 (5432), Redis 7 (6379) 시작
docker-compose down    # 중지
```

### 시크릿 설정
```bash
cp backend/src/main/resources/application-secret.yml.example backend/src/main/resources/application-secret.yml
# DB_PASSWORD, REDIS_PASSWORD, JWT_SECRET 값 입력
```

### 백엔드 실행
```bash
cd backend
./gradlew bootRun      # 개발 서버 실행 (localhost:8080)
./gradlew build        # 빌드
./gradlew test         # 전체 테스트 실행
./gradlew test --tests "com.school.management.domain.auth.AuthServiceTest"  # 단일 테스트 (패키지.클래스명)
./gradlew clean build  # 클린 빌드
```

### 주요 엔드포인트
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Prometheus: http://localhost:8080/actuator/prometheus

## 기술 스택
- **Backend**: Java 21, Spring Boot 3.3, Spring Security, JPA/Hibernate, Flyway, PostgreSQL, Redis
- **Frontend**: React + TypeScript, Tailwind CSS, Chart.js, PWA (미구현)
- **Infra**: Docker, EC2 2대, Vercel, S3, Firebase FCM, AWS SES

## 아키텍처

### 패키지 구조
```
com.school.management
├── global/
│   ├── config        # Security, Redis, S3 등 설정
│   ├── security      # JWT 필터, UserDetails
│   ├── exception     # 전역 예외 처리
│   ├── dto           # 공통 응답 DTO
│   ├── entity        # BaseEntity (공통 필드)
│   └── util
└── domain/
    ├── auth          # 로그인/로그아웃/토큰 재발급
    ├── user          # ADMIN/TEACHER/STUDENT/PARENT
    ├── student       # 학생 정보
    ├── grade         # 성적 (중학교/고등학교 분리)
    ├── feedback      # 학생부 피드백
    ├── counseling    # 상담 기록
    ├── notification  # Firebase FCM + AWS SES
    ├── report        # 성적표/보고서
    └── search        # 통합 검색
```

### 주요 규칙
- **Soft Delete**: 모든 삭제는 `is_deleted` 플래그 사용 (물리 삭제 금지)
- **JWT**: Access Token 30분 / Refresh Token 7일, Redis에 Refresh Token 저장
- **RBAC**: ADMIN / TEACHER / STUDENT / PARENT 권한 분리
- **성적 체계**:
  - 중학교: A/B/C 등급, 석차등급 없음
  - 고등학교: A~E 등급, 석차등급 1~9등급
- **DB 마이그레이션**: Flyway 사용, DDL auto는 `validate` (직접 스키마 변경 금지, 반드시 migration 파일 작성)
- **API 문서**: SpringDoc OpenAPI (Swagger), 컨트롤러에 어노테이션 추가 필요
- **DB 마이그레이션 파일 위치**: `backend/src/main/resources/db/migration/` (파일명: `V{버전}__{설명}.sql`)

## 외부 서비스 설정 (application-secret.yml)
- `DB_PASSWORD`: PostgreSQL 비밀번호
- `REDIS_PASSWORD`: Redis 비밀번호 (기본값: redis1234)
- `JWT_SECRET`: JWT 서명 키
- AWS 자격증명 (S3, SES 사용 시)
- Firebase 서비스 계정 키 (FCM 사용 시)

=== API 명세서 ===

Base URL: /api/v1 (예상)
공통 응답: ApiResponse<T> { data, message, status }

--- 인증 ---
POST   /auth/login              로그인 (모든 사용자)
POST   /auth/logout             로그아웃
POST   /auth/refresh            토큰 재발급
POST   /auth/password/reset     비밀번호 초기화 요청

--- 사용자 관리 (ADMIN) ---
GET    /users/{id}              사용자 상세 조회
PATCH  /users/{id}/role         역할 변경

--- 학생 관리 ---
GET    /students                학생 목록 조회 (grade, class, name, page, size 필터) [교사]
GET    /students/{id}           학생 상세 조회 [교사]
POST   /students                학생 등록 [교사]
PUT    /students/{id}           학생 수정 [교사]
DELETE /students/{id}           학생 삭제 [ADMIN]

--- 학부모 매핑 ---
GET    /students/{id}/parents   학부모 목록 조회 (학생별) [교사]
POST   /parent-student          학부모-학생 매핑 추가
DELETE /parent-student/{id}     학부모-학생 매핑 삭제

--- 교과목 ---
GET    /subjects                교과목 목록 조회
POST   /subjects                교과목 등록
PUT    /subjects/{id}           교과목 수정
DELETE /subjects/{id}           교과목 삭제

--- 성적 ---
GET    /students/{id}/grades    학생 성적 조회
POST   /grades                  성적 입력 [교사]
PUT    /grades/{id}             성적 수정 [교사]

--- 학기 요약 ---
GET    /students/{id}/semester-summary   학기별 성적 요약 조회

--- 출결 ---
GET    /students/{id}/attendance         출결 내역 조회
GET    /students/{id}/attendance/summary 출결 요약
POST   /attendance                       출결 등록 [교사]

--- 학생부 ---
GET    /students/{id}/records            학생부 조회
POST   /students/{id}/records            학생부 항목 추가 [교사]

--- 피드백 ---
POST   /feedbacks                        피드백 작성 [교사]
PATCH  /feedbacks/{id}/visibility        피드백 공개 설정 변경

--- 상담 ---
GET    /counselings                      상담 내역 조회
GET    /counselings/search               전체 상담 검색 (교사 간 공유)
POST   /counselings                      상담 등록 [교사]

--- 통합 검색 ---
GET    /search                           학생별 통합 검색

--- 알림 ---
GET    /notifications                    알림 목록 조회
PATCH  /notifications/read               알림 읽음 처리
GET    /notifications/settings           알림 설정 조회
PUT    /notifications/settings           알림 설정 변경

--- 보고서 ---
GET    /reports                          보고서 목록 조회
POST   /reports/grade-analysis           성적 분석 보고서 생성
GET    /reports/{id}/download            보고서 다운로드

=== 요구사항 명세 ===

## 주요 요구사항

### 기능적 요구사항

#### 1. 학생 성적 관리
- 각 학생의 학기별 성적을 기록하고 수정할 수 있어야 한다.
- 성적 입력 후 자동으로 총점, 평균 점수, 등급 등을 계산하여 표시해야 한다.
- 학생별 과목별 성적을 확인할 수 있어야 한다.
- 전 교과목 성적을 레이더 차트로 시각화할 수 있어야 한다.

#### 2. 학생부 관리
- 학생의 기본 정보(이름, 학년, 반, 번호 등) 및 성적 외의 정보(출결, 특기사항 등)를 기록할 수 있어야 한다.
- 학생부 항목을 교사가 수정/추가할 수 있어야 한다.

#### 3. 피드백 제공
- 교사는 각 학생에게 피드백을 작성하고 저장할 수 있어야 한다.
- 피드백은 학생의 성적뿐 아니라 행동, 출결, 태도 등 다양한 항목에 대해 작성할 수 있어야 한다.
- 작성된 피드백은 학생 및 학부모에게 제공할 수 있는 옵션을 제공해야 한다.

#### 4. 상담 내역 관리
- 교사는 학생과의 상담 내역을 기록하고, 각 상담에 대한 날짜, 주요 내용, 다음 상담 계획 등을 입력할 수 있어야 한다.
- 상담 내역은 다른 교사들과 공유될 수 있도록 해야 한다.
- 교사 간 상담 내역을 검색하고 필터링할 수 있는 기능을 제공해야 한다.

#### 5. 학생 정보 검색 및 조회
- 교사는 학생의 성적, 상담 내역, 피드백 등을 손쉽게 검색할 수 있어야 한다.
- 학생의 성적/상담/피드백 등을 기간별, 과목별로 필터링할 수 있어야 한다.

#### 6. 알림 기능
- 성적 입력, 피드백 작성, 상담 내역 업데이트 등 중요 사항에 대한 알림을 제공해야 한다.
- 학부모와 학생에게 성적이나 피드백이 업데이트되면 자동으로 알림이 전송되도록 해야 한다.

### 비기능적 요구사항

#### 1. 사용자 관리
- 교사, 학생, 학부모 등 다양한 사용자 계정을 관리할 수 있어야 한다.
- 각 사용자별로 권한을 설정하고, 접근할 수 있는 데이터에 차등을 둘 수 있어야 한다.

#### 2. 데이터 보안
- 학생들의 개인정보 및 성적 정보는 암호화하여 안전하게 저장되어야 한다.
- 각 교사 간에 학생의 상담 내역 및 성적 정보는 권한에 따라 접근을 제한해야 한다.

#### 3. 시스템 안정성 및 성능
- 시스템은 동시에 여러 교사가 접속하여 데이터를 입력해도 원활히 동작해야 한다.
- 데이터는 실시간으로 업데이트되어야 하며, 반영이 지연되지 않도록 해야 한다.

#### 4. 백업 및 복구
- 모든 학생 정보 및 데이터는 정기적으로 백업되어야 한다.
- 시스템 장애 발생 시, 데이터를 복구할 수 있는 기능이 제공되어야 한다.

### 기타 요구사항

#### 1. 인터페이스
- 시스템은 웹 애플리케이션 형태로 제공되어야 하며, 모바일 기기에서도 사용이 가능해야 한다.
- 사용자 친화적인 UI/UX 설계가 필요하다. (직관적인 메뉴, 명확한 버튼 등)

#### 2. 보고서 생성
- 성적 분석 보고서, 상담 내역 보고서, 피드백 요약 보고서 등 다양한 보고서를 자동으로 생성할 수 있어야 한다.
- 교사는 보고서를 PDF나 Excel 파일 형식으로 다운로드할 수 있어야 한다.

## 우선순위
- 학생 성적 관리: 우선순위 - 높음
- 학생부 관리: 우선순위 - 높음
- 피드백 제공: 우선순위 - 중간
- 상담 내역 관리: 우선순위 - 중간
- 알림 기능: 우선순위 - 중간
- 보고서 생성: 우선순위 - 낮음

## 사용자 시나리오
1. 교사 A가 성적을 입력하고 학생의 피드백을 작성하는 시나리오
   - 교사 A는 학생의 성적을 입력하고, 해당 학생에게 긍정적인 피드백을 작성하여 저장한다.
2. 교사 B가 학생 상담 내역을 조회하는 시나리오
   - 교사 B는 해당 학생의 상담 내역을 조회하여 이전 상담의 내용을 확인하고, 후속 상담 계획을 세운다.
3. 학부모가 자녀의 성적과 피드백을 확인하는 시나리오
   - 학부모는 자녀의 성적을 조회하고, 교사가 작성한 피드백을 확인한다.

## 검증 기준
- 기능 테스트: 성적 입력, 상담 내역 등록, 피드백 작성 등의 기능이 정상적으로 동작하는지 테스트
- 보안 테스트: 학생 정보가 암호화되어 안전하게 저장되고, 권한 없는 사용자가 접근할 수 없는지 테스트
- 성능 테스트: 다수의 교사가 동시에 시스템에 접속할 수 있는지 테스트
