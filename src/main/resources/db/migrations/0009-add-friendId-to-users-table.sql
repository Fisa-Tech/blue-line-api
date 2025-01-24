-- liquibase formatted sql

-- changeset aurel:1737740720401-1
ALTER TABLE users ADD friend_id VARCHAR(255);

-- changeset aurel:1737740720401-2
ALTER TABLE users ADD CONSTRAINT UC_USERSFRIEND_ID_COL UNIQUE (friend_id);