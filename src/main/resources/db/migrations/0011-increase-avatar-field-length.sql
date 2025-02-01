-- liquibase formatted sql

-- changeset aurel:1738451427408-1
ALTER TABLE users ALTER COLUMN avatar TYPE VARCHAR(750) USING (avatar::VARCHAR(750));

