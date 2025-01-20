-- liquibase formatted sql

-- changeset aurel:1733395243146-1
ALTER TABLE relationships DROP CONSTRAINT uc_relationshipsrequest_status_col;

