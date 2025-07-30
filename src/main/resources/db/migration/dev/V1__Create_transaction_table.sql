CREATE TABLE IF NOT EXISTS bankschema.transaction (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    is_bought BOOLEAN NOT NULL,
    currency VARCHAR(50),
    amount NUMERIC(19, 4),
    transaction_time TIMESTAMP,
    
    CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id) REFERENCES bankschema.app_user(id)
);
