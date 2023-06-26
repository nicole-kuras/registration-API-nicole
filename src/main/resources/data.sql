-- $2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q ===> 'pass'
INSERT IGNORE INTO users (username, password, role, enabled, email)
VALUES ('admin', '$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q', 'ROLE_ADMIN', TRUE, 'admin@example.com'),
       ('patient', '$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q', 'ROLE_PATIENT', TRUE, 'patient@example.com'),
       ('doctor', '$2a$10$f6K9roeLJp3z97lnJ0mu2.QtMMk.3HmTPB4kT9xsau0Ll.nbvJ55q', 'ROLE_DOCTOR', TRUE, 'doctor@example.com');