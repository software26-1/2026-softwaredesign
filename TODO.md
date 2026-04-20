# 진행상황 (2026-04-08)

## 오늘 한 것

### feat/#29 — Subject CRUD API ✅ PR 머지
- `SubjectCreateRequest` / `SubjectUpdateRequest` / `SubjectResponse` DTO 추가
- `SubjectService` 구현 — 전체 조회, 등록, 수정, 소프트 삭제
- `SubjectController` — GET/POST/PUT/DELETE /api/subjects

### feat/#30 — 레이더 차트 API 구현 ✅ PR 머지
- `GradeRadarResponse` DTO 추가
- `GradeService.getStudentRadarGrades()` 추가
- `GET /api/grades/students/{id}/radar` 엔드포인트 추가

### feat/#34 — 레이더 차트 API 개선 (PR 올림, 머지 대기)
- `GET /api/grades/students/{id}/radar?semester=` 학기 파라미터 추가
- `RedisConfig` 추가 — RedisCacheManager TTL 10분
- `@Cacheable` 적용 (캐시 키: `grades:radar:{studentId}:{semester}`)
- 성적 등록/수정/삭제 시 `@CacheEvict` 캐시 무효화
- `GradeRepository` 학기별 성적 조회 쿼리 추가
- `GradeRadarResponse` Jackson `@JsonCreator` 생성자 추가

### setting/#36 — 프론트엔드 초기 세팅 ✅ PR 머지
- Vite + React 18 + TypeScript 프로젝트 생성
- Tailwind CSS v4 설정
- Chart.js 4.x + react-chartjs-2 설치
- axios API 클라이언트 (JWT 인터셉터, 401 자동 처리)
- 폴더 구조: pages/, components/, hooks/, api/, types/, utils/
- .env.example 추가

---

## 다음 할 것 (FE)

> ⚠️ 작업 순서: GitHub에서 이슈 먼저 생성 → 이슈 번호로 브랜치 파기 → 작업 시작
> 브랜치명 규칙: `feat/#이슈번호`

| 우선순위 | 이슈 | 내용 |
|---------|------|------|
| 1 | ~~feat/#34 PR 머지 확인~~ | 레이더 차트 개선 머지 후 main 동기화 |
| 2 | [FE] 학생 목록/상세 + 학생부 관리 UI | 학생 목록(필터·페이징), 학생 상세, 학생부 항목 CRUD |
| 3 | [FE] 성적 입력 테이블 + Chart.js 레이더 차트 UI | 성적 입력 테이블, 과목별 레이더 차트 시각화 |

---

## 현재 구현 완료된 API

| Method | URL | 상태 |
|--------|-----|------|
| POST | /api/auth/login | ✅ |
| POST | /api/auth/logout | ✅ |
| POST | /api/auth/refresh | ✅ |
| GET | /api/students | ✅ |
| GET | /api/students/{id} | ✅ |
| POST | /api/students | ✅ |
| PUT | /api/students/{id} | ✅ |
| DELETE | /api/students/{id} | ✅ |
| POST | /api/grades | ✅ |
| GET | /api/grades/students/{id} | ✅ |
| PUT | /api/grades/{id} | ✅ |
| DELETE | /api/grades/{id} | ✅ |
| GET | /api/grades/students/{id}/summary | ✅ |
| GET | /api/grades/students/{id}/radar | ✅ (semester 파라미터 + Redis 캐싱 — feat/#34 머지 대기) |
| GET | /api/subjects | ✅ |
| POST | /api/subjects | ✅ |
| PUT | /api/subjects/{id} | ✅ |
| DELETE | /api/subjects/{id} | ✅ |
| GET/POST/PUT/DELETE | /api/students/{id}/records | ✅ |
| GET/POST/PUT/DELETE | /api/attendance | ✅ |

## 미구현 API (백엔드)

| Method | URL | 비고 |
|--------|-----|------|
| POST | /api/feedbacks | 피드백 작성 |
| PATCH | /api/feedbacks/{id}/visibility | 피드백 공개 설정 |
| GET/POST | /api/counselings | 상담 내역 |
| GET | /api/counselings/search | 상담 검색 |
| GET | /api/notifications | 알림 목록 |
| PATCH | /api/notifications/read | 알림 읽음 처리 |
| GET/PUT | /api/notifications/settings | 알림 설정 |
| GET | /api/search | 통합 검색 |
| GET/POST | /api/reports | 보고서 |
