CREATE TABLE wealth (
    user_id BIGINT PRIMARY KEY
);

-- Create the collection table for the wealthMap
CREATE TABLE wealth_wealth_map (
    wealth_user_id BIGINT NOT NULL,
    wealth_map VARCHAR(10) NOT NULL,
    wealth_map_key VARCHAR(10) NOT NULL,
    CONSTRAINT fk_wealth_map_user_id FOREIGN KEY (wealth_user_id) REFERENCES wealth(user_id)
);

-- The wealth_map column stores the BigDecimal value
-- The wealth_map_key column stores the String key (currency)
ALTER TABLE wealth_wealth_map MODIFY wealth_map DECIMAL(19,4) NOT NULL;

-- Add index for better performance
CREATE INDEX idx_wealth_map_user_id ON wealth_wealth_map(wealth_user_id);
