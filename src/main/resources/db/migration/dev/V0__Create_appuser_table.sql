CREATE TABLE IF NOT EXISTS bankschema.app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    tcno VARCHAR(255)
);
