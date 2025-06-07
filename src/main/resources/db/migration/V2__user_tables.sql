
CREATE TABLE IF NOT EXISTS user_data.user (
                                              id             UUID               PRIMARY KEY DEFAULT gen_random_uuid(),
                                              email          VARCHAR(255)       NOT NULL UNIQUE,
                                              firstname      VARCHAR(100)       NOT NULL,
                                              lastname       VARCHAR(100)       NOT NULL,
                                              user_type      VARCHAR(25)        NOT NULL,
                                              account_status VARCHAR(25)        NOT NULL,
                                              keycloak_id    VARCHAR(100)       NOT NULL UNIQUE,
                                              created_at     TIMESTAMP          NOT NULL DEFAULT NOW(),
                                              updated_at     TIMESTAMP          NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_data.candidate (
                                                   id             UUID PRIMARY KEY REFERENCES user_data.user(id) ON DELETE CASCADE,
                                                   phone_number   VARCHAR(50) NOT NULL,
                                                   linkedin_link  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_data.recruiter (
                                                   id             UUID PRIMARY KEY REFERENCES user_data.user(id) ON DELETE CASCADE,
                                                   company_id     UUID NOT NULL REFERENCES user_data.company(id),
                                                   position       VARCHAR(100) NOT NULL,
                                                   company_role   VARCHAR(25) NOT NULL
);
