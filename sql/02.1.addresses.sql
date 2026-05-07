CREATE TABLE addresses
(
    address_id       UUID PRIMARY KEY,
    user_id          UUID                  NOT NULL,
    address_name     VARCHAR(100)          NOT NULL,
    receiver_name    VARCHAR(30)           NOT NULL,
    receiver_phone   VARCHAR(15)           NOT NULL,
    receiver_zipcode VARCHAR(5)            NOT NULL,
    receiver_addr1   VARCHAR(100)          NOT NULL,
    receiver_addr2   VARCHAR(200),
    is_default       BOOLEAN DEFAULT FALSE NOT NULL,
    created_at       TIMESTAMP             NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);
