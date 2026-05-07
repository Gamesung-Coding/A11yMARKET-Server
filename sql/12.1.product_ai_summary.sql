CREATE TABLE product_ai_summary
(
    product_id    UUID PRIMARY KEY,
    summary_text  TEXT,
    usage_context TEXT,
    usage_method  TEXT,
    generated_at  TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT fk_product_ai_summary_product FOREIGN KEY (product_id)
        REFERENCES products (product_id) ON DELETE CASCADE
);
