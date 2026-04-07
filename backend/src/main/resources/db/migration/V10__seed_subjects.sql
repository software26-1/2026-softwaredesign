-- V10__seed_subjects.sql
-- 2022 개정 교육과정 기준 교과목 seed 데이터
-- grade_type: RELATIVE(석차등급+성취도A~E) / ABSOLUTE(성취도만) / PASS_FAIL(이수/미이수)

-- =============================================
-- 중학교 교과목 (school_type=MIDDLE, grade_type=ABSOLUTE)
-- =============================================

-- 필수
INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
('MIDDLE', '필수', '국어',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '수학',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '영어',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '사회',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '역사',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '도덕',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '과학',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '기술·가정', 0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '정보',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '체육',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '음악',      0, false, 'ABSOLUTE'),
('MIDDLE', '필수', '미술',      0, false, 'ABSOLUTE');

-- 선택
INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
('MIDDLE', '선택', '한문',        0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '환경',        0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '보건',        0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '진로와 직업', 0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '일본어',      0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '중국어',      0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '독일어',      0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '프랑스어',    0, false, 'ABSOLUTE'),
('MIDDLE', '선택', '스페인어',    0, false, 'ABSOLUTE');

-- =============================================
-- 고등학교 공통과목
-- 체육 교과는 ABSOLUTE, 나머지는 RELATIVE
-- =============================================

INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
('HIGH', '공통', '공통국어1',     4, false, 'RELATIVE'),
('HIGH', '공통', '공통국어2',     4, false, 'RELATIVE'),
('HIGH', '공통', '공통수학1',     4, false, 'RELATIVE'),
('HIGH', '공통', '공통수학2',     4, false, 'RELATIVE'),
('HIGH', '공통', '공통영어1',     4, false, 'RELATIVE'),
('HIGH', '공통', '공통영어2',     4, false, 'RELATIVE'),
('HIGH', '공통', '통합사회1',     4, false, 'RELATIVE'),
('HIGH', '공통', '통합사회2',     4, false, 'RELATIVE'),
('HIGH', '공통', '통합과학1',     4, false, 'RELATIVE'),
('HIGH', '공통', '통합과학2',     4, false, 'RELATIVE'),
('HIGH', '공통', '과학탐구실험1', 2, false, 'RELATIVE'),
('HIGH', '공통', '과학탐구실험2', 2, false, 'RELATIVE'),
('HIGH', '공통', '공통체육1',     2, false, 'ABSOLUTE'),
('HIGH', '공통', '공통체육2',     2, false, 'ABSOLUTE');

-- =============================================
-- 고등학교 일반선택 - RELATIVE (석차등급 O)
-- =============================================

INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
-- 국어
('HIGH', '일반선택', '화법과 언어',      4, false, 'RELATIVE'),
('HIGH', '일반선택', '독서와 작문',      4, false, 'RELATIVE'),
('HIGH', '일반선택', '문학',             4, false, 'RELATIVE'),
-- 수학
('HIGH', '일반선택', '대수',             4, false, 'RELATIVE'),
('HIGH', '일반선택', '미적분Ⅰ',         4, false, 'RELATIVE'),
('HIGH', '일반선택', '확률과 통계',      4, false, 'RELATIVE'),
-- 영어
('HIGH', '일반선택', '영어Ⅰ',           4, false, 'RELATIVE'),
('HIGH', '일반선택', '영어Ⅱ',           4, false, 'RELATIVE'),
('HIGH', '일반선택', '영어 독해와 작문', 4, false, 'RELATIVE'),
-- 사회
('HIGH', '일반선택', '세계시민과 지리',  4, false, 'RELATIVE'),
('HIGH', '일반선택', '세계사',           4, false, 'RELATIVE'),
('HIGH', '일반선택', '사회와 문화',      4, false, 'RELATIVE'),
('HIGH', '일반선택', '현대사회와 윤리',  4, false, 'RELATIVE'),
-- 과학
('HIGH', '일반선택', '역학과 에너지',    4, false, 'RELATIVE'),
('HIGH', '일반선택', '전자기와 빛',      4, false, 'RELATIVE'),
('HIGH', '일반선택', '화학 반응의 세계', 4, false, 'RELATIVE'),
('HIGH', '일반선택', '세포와 물질대사',  4, false, 'RELATIVE'),
('HIGH', '일반선택', '지구시스템과학',   4, false, 'RELATIVE'),
('HIGH', '일반선택', '행성우주과학',     4, false, 'RELATIVE'),
-- 기술·가정/정보
('HIGH', '일반선택', '기술·가정', 4, false, 'RELATIVE'),
('HIGH', '일반선택', '정보',      4, false, 'RELATIVE'),
-- 제2외국어
('HIGH', '일반선택', '독일어',   4, false, 'RELATIVE'),
('HIGH', '일반선택', '프랑스어', 4, false, 'RELATIVE'),
('HIGH', '일반선택', '스페인어', 4, false, 'RELATIVE'),
('HIGH', '일반선택', '중국어',   4, false, 'RELATIVE'),
('HIGH', '일반선택', '일본어',   4, false, 'RELATIVE'),
('HIGH', '일반선택', '러시아어', 4, false, 'RELATIVE'),
('HIGH', '일반선택', '아랍어',   4, false, 'RELATIVE'),
('HIGH', '일반선택', '베트남어', 4, false, 'RELATIVE'),
-- 한문
('HIGH', '일반선택', '한문', 4, false, 'RELATIVE');

-- =============================================
-- 고등학교 일반선택 - ABSOLUTE (체육·예술: 석차등급 X)
-- =============================================

INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
('HIGH', '일반선택', '운동과 건강', 4, false, 'ABSOLUTE'),
('HIGH', '일반선택', '음악',        4, false, 'ABSOLUTE'),
('HIGH', '일반선택', '미술',        4, false, 'ABSOLUTE'),
('HIGH', '일반선택', '연극',        4, false, 'ABSOLUTE');

-- =============================================
-- 고등학교 일반선택 - PASS_FAIL (교양 교과)
-- =============================================

INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
('HIGH', '일반선택', '인간과 철학',  4, false, 'PASS_FAIL'),
('HIGH', '일반선택', '논리와 사고',  4, false, 'PASS_FAIL'),
('HIGH', '일반선택', '인간과 심리',  4, false, 'PASS_FAIL'),
('HIGH', '일반선택', '교육의 이해',  4, false, 'PASS_FAIL'),
('HIGH', '일반선택', '삶과 종교',    4, false, 'PASS_FAIL'),
('HIGH', '일반선택', '진로와 직업',  4, false, 'PASS_FAIL');

-- =============================================
-- 고등학교 진로선택 (ABSOLUTE, 성취도 A~C)
-- =============================================

INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
-- 국어
('HIGH', '진로선택', '주제 탐구 독서',    4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '문학과 영상',       4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '직무 의사소통',     4, true, 'ABSOLUTE'),
-- 수학
('HIGH', '진로선택', '미적분Ⅱ',          4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '기하',              4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '경제 수학',         4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '인공지능 수학',     4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '직무 수학',         4, true, 'ABSOLUTE'),
-- 영어
('HIGH', '진로선택', '영어 발표와 토론',  4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '심화 영어',         4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '영미 문학 읽기',    4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '직무 영어',         4, true, 'ABSOLUTE'),
-- 사회
('HIGH', '진로선택', '한국지리 탐구',            4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '도시의 미래 탐구',         4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '동아시아 역사 기행',       4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '역사로 탐구하는 현대세계', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '정치',                     4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '법과 사회',                4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '경제',                     4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '윤리문제 탐구',            4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '국제 관계의 이해',         4, true, 'ABSOLUTE'),
-- 과학
('HIGH', '진로선택', '물질과 에너지',      4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '세포와 물질대사 심화', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '지구시스템과학 심화', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '기후변화와 환경생태', 4, true, 'ABSOLUTE'),
-- 체육
('HIGH', '진로선택', '스포츠 생활1', 2, true, 'ABSOLUTE'),
('HIGH', '진로선택', '스포츠 생활2', 2, true, 'ABSOLUTE'),
('HIGH', '진로선택', '체육 탐구',    4, true, 'ABSOLUTE'),
-- 예술
('HIGH', '진로선택', '음악 연주와 창작', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '음악 감상과 비평', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '미술 창작',        4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '미술 감상과 비평', 4, true, 'ABSOLUTE'),
-- 기술·가정/정보
('HIGH', '진로선택', '로봇과 공학세계',  4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '생활과학 탐구',    4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '아동발달과 부모',  4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '생애설계와 자립',  4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '인공지능 기초',    4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '데이터 과학',      4, true, 'ABSOLUTE'),
-- 제2외국어 회화
('HIGH', '진로선택', '독일어 회화',   4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '프랑스어 회화', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '스페인어 회화', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '중국어 회화',   4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '일본어 회화',   4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '러시아어 회화', 4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '아랍어 회화',   4, true, 'ABSOLUTE'),
('HIGH', '진로선택', '베트남어 회화', 4, true, 'ABSOLUTE'),
-- 한문
('HIGH', '진로선택', '한문 고전 읽기', 4, true, 'ABSOLUTE');

-- =============================================
-- 고등학교 융합선택 (ABSOLUTE, 성취도 A~C)
-- =============================================

INSERT INTO subject (school_type, category, subject_name, credits, is_career_elective, grade_type) VALUES
-- 국어
('HIGH', '융합선택', '문학과 매체',   4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '언어생활 탐구', 4, true, 'ABSOLUTE'),
-- 수학
('HIGH', '융합선택', '수학과 문화',   4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '실용 통계',     4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '수학과제 탐구', 4, true, 'ABSOLUTE'),
-- 영어
('HIGH', '융합선택', '실생활 영어 회화', 4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '미디어 영어',      4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '세계 문화와 영어', 4, true, 'ABSOLUTE'),
-- 사회
('HIGH', '융합선택', '여행지리',                   4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '역사와 문화',                4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '사회문제 탐구',              4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '금융과 경제생활',            4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '윤리와 사상',                4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '기후변화와 지속가능한 세계', 4, true, 'ABSOLUTE'),
-- 과학
('HIGH', '융합선택', '과학의 역사와 문화',    4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '기후변화와 환경생태 탐구', 4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '융합과학 탐구',         4, true, 'ABSOLUTE'),
-- 체육
('HIGH', '융합선택', '스포츠 문화', 4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '스포츠 과학', 4, true, 'ABSOLUTE'),
-- 예술
('HIGH', '융합선택', '음악과 미디어', 4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '미술과 매체',   4, true, 'ABSOLUTE'),
-- 기술·가정/정보
('HIGH', '융합선택', '창의 공학 설계',    4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '지식 재산 일반',    4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '미래 기술과 세상',  4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '가정과학',          4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '소프트웨어와 생활', 4, true, 'ABSOLUTE'),
-- 교양
('HIGH', '융합선택', '인문학과 경제',          4, true, 'ABSOLUTE'),
('HIGH', '융합선택', '미디어 리터러시와 보건', 4, true, 'ABSOLUTE');
