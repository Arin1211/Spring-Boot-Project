-- Add foreign key constraints for referential integrity
ALTER TABLE transaction
ADD CONSTRAINT fk_transaction_user_id
FOREIGN KEY (user_id) REFERENCES app_user(id);

ALTER TABLE transfer
ADD CONSTRAINT fk_transfer_from_user_id
FOREIGN KEY (from_user_id) REFERENCES app_user(id);

ALTER TABLE transfer
ADD CONSTRAINT fk_transfer_to_user_id
FOREIGN KEY (to_user_id) REFERENCES app_user(id);

ALTER TABLE wealth
ADD CONSTRAINT fk_wealth_user_id
FOREIGN KEY (user_id) REFERENCES app_user(id);
