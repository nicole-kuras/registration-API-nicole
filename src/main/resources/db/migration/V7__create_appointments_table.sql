CREATE TABLE IF NOT EXISTS appointments
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_and_time TIMESTAMP NOT NULL,
    patient_id    BIGINT    NOT NULL,
    doctor_id     BIGINT    NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors (id) ON DELETE CASCADE
)