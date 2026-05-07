CREATE TABLE sellers
(
    seller_id            UUID PRIMARY KEY,
    user_id              UUID                  NOT NULL,
    seller_name          VARCHAR(255)          NOT NULL,
    business_number      VARCHAR(50)           NOT NULL,
    seller_grade         VARCHAR(30)           NOT NULL,
    seller_intro         VARCHAR(1024),
    is_a11y_guarantee    BOOLEAN DEFAULT FALSE NOT NULL,
    seller_submit_status VARCHAR(20)           NOT NULL,
    submit_date          TIMESTAMP             NOT NULL,
    approved_date        TIMESTAMP,
    updated_at           TIMESTAMP             NOT NULL,
    CONSTRAINT fk_seller_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);
