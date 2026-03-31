CREATE TABLE report (
                        id         BIGSERIAL    PRIMARY KEY,
                        user_id    BIGINT       NOT NULL REFERENCES users(id),
                        type       VARCHAR(50)  NOT NULL,
                        file_url   VARCHAR(512) NOT NULL,
                        status     VARCHAR(20)  NOT NULL,
                        expires_at TIMESTAMP,
                        created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
                        is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);
