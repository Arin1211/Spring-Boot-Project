CREATE TABLE transfer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    currency VARCHAR(10) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    transfer_time TIMESTAMP NOT NULL
);

-- Add indexes for better query performance
CREATE INDEX idx_transfer_from_user_id ON transfer(from_user_id);
CREATE INDEX idx_transfer_to_user_id ON transfer(to_user_id);
CREATE INDEX idx_transfer_time ON transfer(transfer_time);
