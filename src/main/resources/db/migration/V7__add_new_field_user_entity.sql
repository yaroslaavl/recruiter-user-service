ALTER TABLE user_data."user"
ADD COLUMN is_temporary_blocked BOOLEAN NOT NULL DEFAULT FALSE;