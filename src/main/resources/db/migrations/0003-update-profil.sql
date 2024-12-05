-- liquibase formatted sql

-- changeset mathi:1733390840793-1
ALTER TABLE users ADD firstname VARCHAR(255) NOT NULL;

-- changeset mathi:1733390840793-2
ALTER TABLE users ADD lastname VARCHAR(255) NOT NULL;

-- changeset mathi:1733390840793-3
ALTER TABLE users ADD sex VARCHAR(255);

-- changeset mathi:1733390840793-4
ALTER TABLE users DROP CONSTRAINT users_username_key;

-- changeset mathi:1733390840793-5
ALTER TABLE users DROP COLUMN address;

-- changeset mathi:1733390840793-6
ALTER TABLE users DROP COLUMN is_admin;

-- changeset mathi:1733390840793-7
ALTER TABLE users DROP COLUMN sexe;

-- changeset mathi:1733390840793-8
ALTER TABLE users DROP COLUMN username;

