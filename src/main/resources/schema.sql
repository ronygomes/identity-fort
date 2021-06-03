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

CREATE TABLE IF NOT EXISTS verification_tokens (
    token VARCHAR(100) PRIMARY KEY,
    user_id INT NOT NULL,
	expiry_date TIMESTAMP NOT NULL,
	type VARCHAR(20) NOT NULL,
	FOREIGN KEY(user_id) REFERENCES users(id)
);

-- Source: https://github.com/spring-projects/spring-security-oauth/blob/main/spring-security-oauth2/src/test/resources/schema.sql
CREATE TABLE IF NOT EXISTS oauth_code (
    code VARCHAR(256),
    authentication BYTEA
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
    token_id VARCHAR(256),
    token BYTEA,
    authentication_id VARCHAR(256) PRIMARY KEY,
    user_name VARCHAR(256),
    client_id VARCHAR(256),
    authentication BYTEA,
    refresh_token VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
    token_id VARCHAR(256),
    token BYTEA,
    authentication BYTEA
);

CREATE TABLE IF NOT EXISTS oauth_approvals (
    userId VARCHAR(256),
    clientId VARCHAR(256),
    scope VARCHAR(256),
    status VARCHAR(10),
    expiresAt TIMESTAMP,
    lastModifiedAt TIMESTAMP
);

 /*
 create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

-- customized oauth_client_details table
create table ClientDetails (
  appId VARCHAR(256) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token LONGVARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

  */