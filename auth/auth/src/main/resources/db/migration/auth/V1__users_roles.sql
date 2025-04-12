CREATE TABLE base_user
(
    id            SERIAL         NOT NULL,
    username      VARCHAR(255)   UNIQUE NOT NULL,
    email         VARCHAR(255)   UNIQUE,
    phone_number  VARCHAR(255)   UNIQUE,
    password_hash VARCHAR(255)

        CHECK (email IS NOT NULL OR phone_number IS NOT NULL),

    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE admin
(
    id BIGINT NOT NULL REFERENCES base_user(id) ON DELETE CASCADE,

    CONSTRAINT pk_admin PRIMARY KEY (id)
);

CREATE TABLE client
(
    id BIGINT NOT NULL REFERENCES base_user(id),
    CONSTRAINT pk_client PRIMARY KEY (id)
);