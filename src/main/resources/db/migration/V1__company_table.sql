CREATE TABLE IF NOT EXISTS user_data.company (
                         id             UUID               PRIMARY KEY DEFAULT gen_random_uuid(),
                         nip            VARCHAR(20)        NOT NULL UNIQUE,
                         name           VARCHAR(255)       NOT NULL,
                         voivodeship    VARCHAR(100)       NOT NULL,
                         city           VARCHAR(100)       NOT NULL,
                         post_code      VARCHAR(25)        NOT NULL,
                         street         VARCHAR(100)       NOT NULL,
                         employee_count INTEGER,
                         description    TEXT,
                         company_status varchar(20)        NOT NULL,
                         created_at     TIMESTAMP          NOT NULL DEFAULT NOW(),
                         updated_at     TIMESTAMP          NOT NULL DEFAULT NOW()
)
