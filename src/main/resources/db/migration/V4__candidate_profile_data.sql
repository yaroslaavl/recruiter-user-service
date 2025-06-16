CREATE TABLE user_data.candidate_profile_data (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        candidate_id UUID UNIQUE NOT NULL REFERENCES user_data.candidate(id) ON DELETE CASCADE,

                                        desiredSalary_pln VARCHAR(25) NOT NULL,
                                        work_mode VARCHAR(50) NOT NULL,
                                        available_hours_per_week INTEGER NOT NULL,
                                        available_from VARCHAR(25) NOT NULL,

                                        created_at TIMESTAMP DEFAULT now(),
                                        updated_at TIMESTAMP DEFAULT now()
);