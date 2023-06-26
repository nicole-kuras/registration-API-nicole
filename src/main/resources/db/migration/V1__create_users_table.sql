CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(25)  NOT NULL,
    enabled  BOOL         NOT NULL DEFAULT FALSE,
    email    VARCHAR(255) NOT NULL,
    CHECK ( role IN ('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_PATIENT'))
);

CREATE INDEX idx_users_email ON users (email);

