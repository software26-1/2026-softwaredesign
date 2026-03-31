CREATE TABLE feedback (
                          id                BIGSERIAL    PRIMARY KEY,
                          teacher_id        BIGINT       NOT NULL REFERENCES users(id),
                          student_id        BIGINT       NOT NULL REFERENCES student(id),
                          category          VARCHAR(30)  NOT NULL,
                          content           TEXT         NOT NULL,
                          share_with_student BOOLEAN     NOT NULL DEFAULT FALSE,
                          share_with_parent  BOOLEAN     NOT NULL DEFAULT FALSE,
                          created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
                          updated_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
                          is_deleted        BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE counseling (
                            id              BIGSERIAL PRIMARY KEY,
                            student_id      BIGINT    NOT NULL REFERENCES student(id),
                            teacher_id      BIGINT    NOT NULL REFERENCES users(id),
                            counseling_date DATE      NOT NULL,
                            content         TEXT,
                            next_plan       TEXT,
                            is_public       BOOLEAN   NOT NULL DEFAULT FALSE,
                            created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                            updated_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                            is_deleted      BOOLEAN   NOT NULL DEFAULT FALSE
);
