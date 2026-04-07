-- V9__alter_subject_grade_schema.sql
-- 중학교/고등학교 내신 체계 반영을 위한 스키마 수정

-- 1. student: 중학교/고등학교 구분 추가
ALTER TABLE student ADD COLUMN school_type VARCHAR(10) NOT NULL DEFAULT 'HIGH';

-- 2. subject: teacher_id NULL 허용 (시스템 기본 과목은 특정 교사에 귀속되지 않음)
ALTER TABLE subject ALTER COLUMN teacher_id DROP NOT NULL;

-- 2. subject: school_type 추가 (MIDDLE / HIGH)
ALTER TABLE subject ADD COLUMN school_type VARCHAR(10);

-- 2. subject: category 길이 확장 (일반선택, 진로선택, 융합선택 등 한글 카테고리 대응)
ALTER TABLE subject ALTER COLUMN category TYPE VARCHAR(30);

-- 2. subject: grade_type 추가
--   RELATIVE  = 석차등급(1~9) + 성취도(A~E) → 고등학교 공통/일반선택(체육·예술·교양 제외)
--   ABSOLUTE  = 성취도만 → 중학교 전체, 고등학교 체육·예술·진로선택·융합선택
--   PASS_FAIL = 이수/미이수 → 고등학교 교양 교과
ALTER TABLE subject ADD COLUMN grade_type VARCHAR(20) NOT NULL DEFAULT 'RELATIVE';

-- 3. grade: 석차등급 추가 (RELATIVE 과목만 사용, 나머지는 NULL)
ALTER TABLE grade ADD COLUMN rank_grade INT;

-- 4. class_statistic: 고등학교 D/E 등급 비율 추가
ALTER TABLE class_statistic ADD COLUMN ratio_d DECIMAL(5, 2);
ALTER TABLE class_statistic ADD COLUMN ratio_e DECIMAL(5, 2);
