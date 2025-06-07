CREATE TABLE IF NOT EXISTS user_data.recruiter_registration_request (
                                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                                                  recruiter_id UUID NOT NULL UNIQUE REFERENCES user_data.user(id) ON DELETE CASCADE,
                                                                  company_id   UUID NOT NULL REFERENCES user_data.company(id) ON DELETE SET NULL,

                                                                  request_status       VARCHAR(20) NOT NULL,
                                                                  reviewed_by          UUID NOT NULL REFERENCES user_data.user(id) ON DELETE SET NULL,
                                                                  reviewed_at          TIMESTAMP,
                                                                  created_at           TIMESTAMP NOT NULL DEFAULT NOW(),
                                                                  updated_at           TIMESTAMP NOT NULL DEFAULT NOW()
);
