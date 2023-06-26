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

-- $2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q ===> 'pass'
INSERT INTO users (id, username, password, role, enabled, email)
VALUES (1, 'admin', '$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q', 'ROLE_ADMIN', TRUE,
        'admin@example.com'),
       (2, 'patient', '$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q', 'ROLE_PATIENT', TRUE,
        'patient@example.com'),
       (3, 'doctor', '$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q', 'ROLE_DOCTOR', TRUE,
        'doctor@example.com');