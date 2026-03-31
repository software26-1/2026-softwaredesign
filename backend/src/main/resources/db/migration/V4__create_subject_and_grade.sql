CREATE TABLE attendance (
    id BIGSERIAL PRIMARY KEY ,
    student_id BIGINT NOT NULL REFERENCES student(id),
    attendance_date DATE NOT NULL ,
    is_attended BOOLEAN NOT NULL DEFAULT TRUE,
    reason VARCHAR(100),
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOOLEAN NOT NULL DEFAULT FALSE
);