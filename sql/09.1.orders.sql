CREATE TABLE orders
(
    order_id         UUID PRIMARY KEY,
    user_id          UUID         NOT NULL,
    user_name        VARCHAR(30)  NOT NULL,
    user_email       VARCHAR(150) NOT NULL,
    user_phone       VARCHAR(15)  NOT NULL,
    receiver_name    VARCHAR(30)  NOT NULL,
    receiver_phone   VARCHAR(15)  NOT NULL,
    receiver_zipcode VARCHAR(5)   NOT NULL,
    receiver_addr1   VARCHAR(100) NOT NULL,
    receiver_addr2   VARCHAR(200),
    total_price      INT          NOT NULL,
    payment_key      VARCHAR(200),
    created_at       TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT chk_user_email_or_phone
        CHECK ( user_email IS NOT NULL
            OR user_phone IS NOT NULL )
);

CREATE INDEX idx_orders_user_id ON orders (user_id);
