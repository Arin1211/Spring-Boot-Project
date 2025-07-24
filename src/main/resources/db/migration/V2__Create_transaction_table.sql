CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    is_bought BOOLEAN NOT NULL,
    currency VARCHAR(10) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    transaction_time TIMESTAMP NOT NULL
);

-- Add index on user_id for better query performance
CREATE INDEX idx_transaction_user_id ON transaction(user_id);
-- Add index on transaction_time for time-based queries
CREATE INDEX idx_transaction_time ON transaction(transaction_time);
