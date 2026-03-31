CREATE TABLE student (
    id         BIGSERIAL    PRIMARY KEY ,
    user_id    BIGINT       NOT NULL REFERENCES users(id),
    name       VARCHAR(100) NOT NULL,
    grade      INT          NOT NULL,
    class_num  INT          NOT NULL,
    number     INT          NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
  );

CREATE TABLE parent_student_mapping (
    id         BIGSERIAL PRIMARY KEY,
    parent_id  BIGINT    NOT NULL REFERENCES users(id),
    student_id BIGINT    NOT NULL REFERENCES student(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN   NOT NULL DEFAULT FALSE
);
