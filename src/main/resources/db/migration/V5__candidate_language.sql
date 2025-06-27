CREATE TABLE user_data.language (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    candidate_id UUID NOT NULL REFERENCES user_data.candidate(id) ON DELETE CASCADE,
                                    language VARCHAR(50) NOT NULL,
                                    language_level VARCHAR(25) NOT NULL,
                                    created_at TIMESTAMP DEFAULT now(),
                                    updated_at TIMESTAMP DEFAULT now()
);