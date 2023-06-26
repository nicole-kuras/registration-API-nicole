ALTER TABLE addresses
    ADD COLUMN doctor_id BIGINT,
    ADD FOREIGN KEY (doctor_id) REFERENCES doctors (id) ON DELETE CASCADE;