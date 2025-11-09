CREATE TABLE users (
   user_id       RAW(16) PRIMARY KEY,
   user_name     VARCHAR2(30) NOT NULL,
   user_pass     VARCHAR2(60),
   user_email    VARCHAR2(254) UNIQUE NOT NULL,
   user_phone    VARCHAR2(15) UNIQUE NOT NULL,
   user_nickname VARCHAR2(100),
   user_role     VARCHAR2(30) NOT NULL,
   created_at    TIMESTAMP DEFAULT current_timestamp,
   updated_at    TIMESTAMP DEFAULT current_timestamp
);

DROP TABLE users;