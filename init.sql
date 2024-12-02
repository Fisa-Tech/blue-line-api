CREATE TABLE users (
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    address  VARCHAR(255) NOT NULL,
    is_admin BOOLEAN      NOT NULL
);

CREATE TABLE events (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    start_date  TIMESTAMP    NOT NULL,
    end_date    TIMESTAMP    NOT NULL,
    organizer_id BIGINT      NOT NULL,
    description TEXT         NOT NULL,
    location    VARCHAR(255) NOT NULL,
    status      VARCHAR(50)  NOT NULL DEFAULT 'PLANNED',
    CONSTRAINT fk_organizer FOREIGN KEY (organizer_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE event_participants (
    event_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    PRIMARY KEY (event_id, user_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
