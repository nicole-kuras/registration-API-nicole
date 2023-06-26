CREATE TABLE IF NOT EXISTS patients
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(50) NOT NULL,
    surname       VARCHAR(50) NOT NULL,
    pesel         VARCHAR(12) NOT NULL,
    date_of_birth DATE        NOT NULL,
    phone_number  VARCHAR(15) NOT NULL,
    user_id       BIGINT      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);