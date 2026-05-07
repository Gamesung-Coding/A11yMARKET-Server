CREATE TABLE categories
(
    category_id   UUID PRIMARY KEY,
    parent_cat_id UUID,
    category_name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_parent_cat FOREIGN KEY (parent_cat_id)
        REFERENCES categories (category_id)
);
