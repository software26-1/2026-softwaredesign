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
