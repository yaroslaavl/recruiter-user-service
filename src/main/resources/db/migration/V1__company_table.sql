CREATE TABLE IF NOT EXISTS user_data.company (
                         id             UUID               PRIMARY KEY DEFAULT gen_random_uuid(),
                         nip            VARCHAR(20)        NOT NULL UNIQUE,
                         name           VARCHAR(255)       NOT NULL,
                         voivodeship    VARCHAR(100),
                         locality       VARCHAR(100),
                         employee_count INTEGER,
                         description    TEXT,
                         company_status varchar(20)        NOT NULL,
                         created_at     TIMESTAMP          NOT NULL DEFAULT NOW()
)
