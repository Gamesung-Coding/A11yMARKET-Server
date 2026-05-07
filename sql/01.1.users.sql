CREATE TABLE users
(
    user_id       UUID PRIMARY KEY,
    user_name     VARCHAR(30) NOT NULL,
    user_pass     VARCHAR(60),
    user_email    VARCHAR(254) UNIQUE,
    user_phone    VARCHAR(15) UNIQUE,
    user_nickname VARCHAR(100),
    user_role     VARCHAR(30) NOT NULL,
    created_at    TIMESTAMP DEFAULT current_timestamp,
    updated_at    TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT chk_user_contact
        CHECK ( user_email IS NOT NULL
            OR user_phone IS NOT NULL )
);
