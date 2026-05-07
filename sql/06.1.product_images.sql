CREATE TABLE product_images
(
    image_id       UUID PRIMARY KEY,
    product_id     UUID          NOT NULL,
    image_url      VARCHAR(2048) NOT NULL,
    alt_text       TEXT,
    created_at     TIMESTAMP DEFAULT current_timestamp,
    image_sequence INT       DEFAULT 1 NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id)
        REFERENCES products (product_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_product_image_sequence ON
    product_images (product_id, image_sequence);
