DELETE FROM users WHERE email = 'john@example.com';

-- password: admin
INSERT INTO users (first_name, last_name, registration_date, email, hashed_password, enabled, locked)
VALUES ('John', 'Doe', NOW(), 'john@example.com', '$2a$10$/mYRRRlNVvGFBZ/psoDwQeBhaQitiEQyRI3wnI.2ERj38k8ds0RRC', true, false);

DELETE FROM applications WHERE client_id = 'test_client';

INSERT INTO applications (client_id, client_secret, redirect_uri, create_date)
VALUES ('test_client', '$2a$10$/mYRRRlNVvGFBZ/psoDwQeBhaQitiEQyRI3wnI.2ERj38k8ds0RRC', 'https://localhost/callback', NOW());