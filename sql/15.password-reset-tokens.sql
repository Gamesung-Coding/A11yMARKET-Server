CREATE TABLE password_reset_tokens (
   id         RAW(16) PRIMARY KEY,
   user_id    RAW(16) NOT NULL
      REFERENCES users ( user_id )
         ON DELETE CASCADE,
   token      VARCHAR(255) NOT NULL UNIQUE,
   created_at TIMESTAMP DEFAULT current_timestamp,
   expires_at TIMESTAMP NOT NULL
);