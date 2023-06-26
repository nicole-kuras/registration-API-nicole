CREATE TABLE IF NOT EXISTS doctors
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    title          VARCHAR(50),
    specialization VARCHAR(255) NOT NULL,
    name           VARCHAR(50)  NOT NULL,
    surname        VARCHAR(50)  NOT NULL,
    date_of_birth  DATE         NOT NULL,
    phone_number   VARCHAR(15)  NOT NULL,
    user_id        BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);