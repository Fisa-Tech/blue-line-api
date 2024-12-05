-- liquibase formatted sql

-- changeset mathi:1733392452149-1
ALTER TABLE users ADD avatar VARCHAR(255);

-- changeset mathi:1733392452149-2
ALTER TABLE users ADD status VARCHAR(255);

