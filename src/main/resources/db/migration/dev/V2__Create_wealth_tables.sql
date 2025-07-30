CREATE TABLE IF NOT EXISTS bankschema.wealth (
    user_id BIGINT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES bankschema.app_user(id)
);

CREATE TABLE IF NOT EXISTS bankschema.wealth_map (
    user_id BIGINT NOT NULL,
    wealth_type VARCHAR(255) NOT NULL,
    amount NUMERIC(19,2) NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, wealth_type),
    FOREIGN KEY (user_id) REFERENCES bankschema.wealth(user_id)
);
