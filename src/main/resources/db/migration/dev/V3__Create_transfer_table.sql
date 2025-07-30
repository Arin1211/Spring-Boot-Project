-- Create sequence for transfer table
CREATE SEQUENCE transfer_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- Create transfer table
CREATE TABLE transfer (
    id BIGINT DEFAULT nextval('transfer_seq') PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    transfer_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_transfer_from_user
        FOREIGN KEY (from_user_id)
        REFERENCES app_user (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_transfer_to_user
        FOREIGN KEY (to_user_id)
        REFERENCES app_user (id)
        ON DELETE CASCADE
);
