CREATE SEQUENCE IF NOT EXISTS users_seq;

CREATE TABLE IF NOT EXISTS users (
    id INT DEFAULT nextval('users_seq') PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    registration_date TIMESTAMP NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    address VARCHAR(1000),
    hashed_password VARCHAR(60),
    enabled BOOLEAN NOT NULL,
    locked BOOLEAN NOT NULL
);

-- Source: org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
CREATE TABLE IF NOT EXISTS persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
	last_used TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS email_verification_tokens (
    token VARCHAR(100) PRIMARY KEY,
    user_id INT NOT NULL,
	expiry_date TIMESTAMP NOT NULL,
	FOREIGN KEY(user_id) REFERENCES users(id)
);