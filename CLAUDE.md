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
./gradlew test --tests "com.school.management.SomeTest"  # 단일 테스트
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
