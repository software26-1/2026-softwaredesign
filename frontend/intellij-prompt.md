# 학생 생활기록부 관리 시스템 - React + TypeScript 프론트엔드 개발

## 📋 프로젝트 요구사항

**진학사랑/나이스 스타일의 학생부 관리 시스템을 React + TypeScript로 개발해주세요.**

---

## 🎯 시스템 아키텍처

### Frontend Stack (반드시 이 스택 사용)
- **Framework**: React 18+
- **Language**: TypeScript
- **Build Tool**: Vite
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Styling**: TailwindCSS
- **State Management**: Context API 또는 Zustand

### Backend (참고용 - 프론트에서 연동할 API)
- **Main API**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Auth**: JWT (accessToken, refreshToken)

---

## 🏗️ 프로젝트 구조

```
frontend/
├── src/
│   ├── components/
│   │   ├── common/          # Button, Input, Table, Card, Badge
│   │   ├── layout/          # Header, Sidebar, MainLayout
│   │   └── features/        # auth, dashboard, grade, feedback, counseling
│   ├── pages/
│   │   ├── LoginPage.tsx
│   │   ├── DashboardPage.tsx
│   │   ├── StudentSearchPage.tsx
│   │   ├── GradeManagementPage.tsx
│   │   ├── FeedbackPage.tsx
│   │   ├── CounselingPage.tsx
│   │   ├── StudentRecordPage.tsx
│   │   └── ReportPage.tsx
│   ├── hooks/               # useAuth, useStudent, useGrade
│   ├── services/            # API 서비스 레이어
│   │   ├── api.ts
│   │   ├── authService.ts
│   │   ├── gradeService.ts
│   │   └── feedbackService.ts
│   ├── types/               # TypeScript 타입 정의
│   │   ├── user.ts
│   │   ├── student.ts
│   │   ├── grade.ts
│   │   └── feedback.ts
│   ├── utils/
│   ├── styles/
│   │   └── globals.css
│   ├── App.tsx
│   └── main.tsx
├── package.json
├── tsconfig.json
├── vite.config.ts
└── tailwind.config.js
```

---

## 🎨 디자인 시스템 (진학사랑/나이스 스타일)

### 색상 팔레트

```css
:root {
  /* Primary Colors - 블루 계열 (교육청 스타일) */
  --primary-blue: #1e5a99;
  --primary-light: #2e6fb8;
  --primary-dark: #0d3a6e;
  --secondary-blue: #4a90d9;
  
  /* Accent */
  --accent-orange: #ff8c42;
  
  /* Backgrounds */
  --bg-gray: #f5f7fa;
  --border-gray: #d4dae2;
  --white: #ffffff;
  
  /* Text */
  --text-dark: #2c3e50;
  --text-gray: #5a6c7d;
  
  /* Status */
  --success-green: #2ecc71;
  --warning-yellow: #f39c12;
  --danger-red: #e74c3c;
}
```

### 타이포그래피
- **Primary Font**: Noto Sans KR
- **Heading 1**: 28px, font-weight: 700
- **Heading 2**: 24px, font-weight: 600
- **Body**: 14px, font-weight: 400

### UI 특징
- 깔끔한 화이트/블루 계열
- 테이블 중심 레이아웃
- 명확한 폼 구조
- 교육청 공신력 느낌

---

## 📊 데이터베이스 구조 (ERD - 20개 테이블)

### 핵심 테이블
1. **user** - 기본 사용자 (login_id, password_hash, role)
2. **teacher** - 교사 (school_id, class_group_id, subject)
3. **student** - 학생 (school_id, class_group_id, student_number, grade, class_number)
4. **parent** - 학부모
5. **admin** - 관리자
6. **school** - 학교 (school_name, school_type, school_code)
7. **class_group** - 학급 (grade, class_number, academic_year)
8. **curriculum** - 교과 대분류
9. **course** - 개설과목 (curriculum_id, teacher_id, course_name, academic_year, semester, 성적비율)
10. **enrollment** - 수강 (student_id, course_id)
11. **grade** - 성적 (enrollment_id, exam_type, score, max_score)
12. **grade_summary** - 성적요약 (raw_score, subject_avg, standard_dev, achievement, rank, grade_level)
13. **feedback** - 피드백 (category: GRADE/BEHAVIOR/ATTENDANCE/ATTITUDE, is_public_to_student, is_public_to_parent)
14. **counseling** - 상담 (counsel_date, main_content, next_plan, is_shared)
15. **student_record** - 학생부 (academic_year, semester, special_note, extracurricular)
16. **parent_student** - 학부모-학생 N:M
17. **approval_request** - 전학/전근 승인
18. **notification** - 알림
19. **report** - 보고서
20. **attendance** - 출결

---

## 🔌 API 명세 요약

### 인증
- `POST /api/auth/login` - 로그인 (userType, loginId, password)
- `POST /api/auth/refresh` - 토큰 갱신

### 성적 관리
- `GET /api/courses/{courseId}/enrollments` - 수강생 목록
- `POST /api/students/{studentId}/grades` - 성적 입력
- `PATCH /api/grades/{gradeId}` - 성적 수정
- `GET /api/students/{studentId}/grade-summary` - 성적 요약

### 피드백
- `POST /api/students/{studentId}/feedbacks` - 피드백 작성
- `GET /api/students/{studentId}/feedbacks` - 피드백 목록
- `PATCH /api/feedbacks/{feedbackId}` - 수정
- `DELETE /api/feedbacks/{feedbackId}` - 삭제

### 상담
- `POST /api/students/{studentId}/counselings` - 상담 등록
- `GET /api/counselings/shared` - 공유된 상담 검색

### 보고서
- `POST /api/reports` - 보고서 생성 요청 (비동기)
- `GET /api/reports/{reportId}/download` - 다운로드

---

## 📱 페이지별 상세 명세

### 1. 로그인 페이지 (`/login`)
**기능**
- 사용자 구분 선택 (교사/학생/학부모)
- 아이디/비밀번호 입력
- JWT 토큰 저장 (localStorage)

**UI 요소**
- 로고 영역 (🦈 아이콘)
- 사용자 구분 Select
- Input 필드 2개
- 로그인 버튼
- 비밀번호 찾기 링크

---

### 2. 대시보드 (`/dashboard`)
**기능**
- 담당 학급 정보
- 통계 카드 (학생 수, 미제출 성적, 미작성 피드백)
- 최근 활동 내역

**UI 요소**
- 4개 통계 카드 (Stats Grid)
- 최근 활동 테이블
- 알림 요약

---

### 3. 학생 검색 (`/students/search`)
**기능**
- 다중 필터 검색 (학년, 반, 이름, 조회내용)
- 검색 결과 테이블
- 학생별 상세보기 모달

**중요 로직**
- 통합 검색: 성적/피드백/상담/출결 한 번에 조회
- 레이더차트 성적 시각화 (선택)

---

### 4. 성적 관리 (`/grades`)
**기능**
- 과목/학기 선택
- 시험 구분 (중간/기말/수행)
- 성적 입력 테이블
- 자동 계산 (평균, 표준편차, 성취도, 석차)

**권한**
- 교과교사: 자기 과목만
- 담임: 자기 반 전체

**UI 요소**
- 과목 선택 카드
- 성적 입력 테이블 (inline input)
- 일괄 저장 버튼

---

### 5. 피드백 작성 (`/feedback`)
**기능**
- 4가지 유형: 성적/행동/출결/태도
- 학생 선택
- 과목 선택 (성적 피드백 시)
- 공개 설정 (학생/학부모)

**UI 요소**
- 피드백 작성 폼
- 작성 내역 테이블

---

### 6. 상담 내역 (`/counseling`)
**기능**
- 상담 등록 (일자, 주요내용, 차후계획)
- 교사 간 공유 설정
- 상담 검색/필터링

**특징**
- 모든 교사가 학교 내 모든 학생 상담 열람 가능
- 담임: 자기 반 최종 관리 권한

---

### 7. 학생부 기록 (`/student-records`)
**기능**
- 특기사항 입력
- 비교과 활동 입력
- 학년도/학기별 관리

---

### 8. 보고서 생성 (`/reports`)
**기능**
- 3종류: 성적분석/상담요약/피드백요약
- PDF/Excel 선택
- 비동기 생성 (202 Accepted)
- 생성 완료 시 다운로드

---

## 🔐 인증 & 권한

### JWT 토큰 관리

```typescript
// services/api.ts
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.VITE_API_URL,
});

// Request Interceptor
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response Interceptor
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      // Refresh token logic
      const refreshToken = localStorage.getItem('refreshToken');
      const response = await api.post('/auth/refresh', { refreshToken });
      localStorage.setItem('accessToken', response.data.accessToken);
      return api(error.config);
    }
    return Promise.reject(error);
  }
);

export default api;
```

---

## 📦 TypeScript 타입 정의

### User Types

```typescript
export enum UserRole {
  TEACHER = 'TEACHER',
  STUDENT = 'STUDENT',
  PARENT = 'PARENT',
  ADMIN = 'ADMIN'
}

export interface User {
  id: number;
  loginId: string;
  name: string;
  role: UserRole;
}

export interface Teacher extends User {
  schoolId: number;
  classGroupId?: number;
  subject?: string;
}
```

### Grade Types

```typescript
export enum ExamType {
  MIDTERM = 'MIDTERM',
  FINAL = 'FINAL',
  TASK = 'TASK'
}

export interface Grade {
  id: number;
  enrollmentId: number;
  examType: ExamType;
  score: number;
  maxScore: number;
}

export interface GradeSummary {
  id: number;
  studentId: number;
  courseId: number;
  courseName: string;
  rawScore: number;
  subjectAvg: number;
  standardDev: number;
  achievement: string;
  rank: number;
  gradeLevel: string;
  numStudents: number;
}
```

### Feedback Types

```typescript
export enum FeedbackCategory {
  GRADE = 'GRADE',
  BEHAVIOR = 'BEHAVIOR',
  ATTENDANCE = 'ATTENDANCE',
  ATTITUDE = 'ATTITUDE'
}

export interface Feedback {
  id: number;
  studentId: number;
  teacherId: number;
  courseId?: number;
  category: FeedbackCategory;
  content: string;
  isPublicToStudent: boolean;
  isPublicToParent: boolean;
  createdAt: string;
}
```

---

## ✅ 개발 순서

### Phase 1: 프로젝트 셋업
1. Vite로 React + TypeScript 프로젝트 생성
2. TailwindCSS 설정 + 디자인 시스템 색상 변수
3. 폴더 구조 설정
4. React Router 설정

### Phase 2: 공통 컴포넌트
1. Button, Input, Table, Card, Badge
2. Header, Sidebar, MainLayout

### Phase 3: 인증 & 대시보드
1. 로그인 페이지
2. JWT 토큰 관리
3. Axios Interceptor
4. 대시보드

### Phase 4: 핵심 기능
1. 학생 검색
2. 성적 관리
3. 피드백
4. 상담 내역
5. 학생부
6. 보고서

---

## 🚀 시작 명령어

```bash
# 프로젝트 생성
npm create vite@latest frontend -- --template react-ts

# 의존성 설치
cd frontend
npm install react-router-dom axios tailwindcss postcss autoprefixer
npm install -D @types/node

# TailwindCSS 초기화
npx tailwindcss init -p

# 개발 서버 실행
npm run dev
```

---

## 💡 중요 사항

1. **모든 API 호출은 services/ 레이어를 통해**
2. **TypeScript 타입 엄격하게 정의**
3. **에러 처리 필수** (try-catch + 사용자 피드백)
4. **로딩 상태 관리**
5. **반응형 디자인** (모바일 고려)
6. **진학사랑/나이스 스타일 유지** (깔끔한 블루/화이트)

---

이 명세서를 기반으로 프로젝트를 시작해주세요!
