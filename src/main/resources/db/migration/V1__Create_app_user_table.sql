CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    tcno VARCHAR(11) NOT NULL
);

-- Add unique constraint on username for better data integrity
ALTER TABLE app_user ADD CONSTRAINT uk_app_user_username UNIQUE (username);
-- Add unique constraint on tcno (Turkish ID number)
ALTER TABLE app_user ADD CONSTRAINT uk_app_user_tcno UNIQUE (tcno);
