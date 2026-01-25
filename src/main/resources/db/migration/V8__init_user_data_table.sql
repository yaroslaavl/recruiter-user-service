INSERT INTO user_data.user (
    id,
    email,
    firstname,
    lastname,
    user_type,
    account_status,
    keycloak_id,
    created_at,
    updated_at,
    is_temporary_blocked
)
VALUES (
           gen_random_uuid(),
           'administrator@recruiter-app.com',
           'Administrator',
           'Testowy',
           'ADMIN',
           'APPROVED',
           '41c80357-f756-41af-ac1f-adff66155d17',
           now(),
           now(),
           false
       ), (gen_random_uuid(),
           'manager@recruiter-app.com',
           'Manager',
           'Testowy',
           'MANAGER',
           'APPROVED',
           'd54d6962-1149-4e92-8b3f-c804d4fcd948',
           now(),
           now(),
           false);