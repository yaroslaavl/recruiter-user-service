CREATE TABLE user_data.language (
                                    id BIGSERIAL PRIMARY KEY,
                                    candidate_id UUID UNIQUE NOT NULL REFERENCES user_data.candidate(id) ON DELETE CASCADE,
                                    language VARCHAR(50) NOT NULL,
                                    language_level VARCHAR(25) NOT NULL
);