ALTER TABLE addresses
    ADD COLUMN patient_id BIGINT,
    ADD FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE;
