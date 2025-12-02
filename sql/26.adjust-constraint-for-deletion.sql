ALTER TABLE addresses DROP CONSTRAINT fk_address_user;
ALTER TABLE addresses
   ADD CONSTRAINT fk_address_user
      FOREIGN KEY ( user_id )
         REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE user_oauth_links DROP CONSTRAINT fk_user_oauth_links_user;
ALTER TABLE user_oauth_links
   ADD CONSTRAINT fk_user_oauth_links_user
      FOREIGN KEY ( user_id )
         REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE default_addresses DROP CONSTRAINT fk_default_users;
ALTER TABLE default_addresses
   ADD CONSTRAINT fk_default_users
      FOREIGN KEY ( user_id )
         REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE sellers DROP CONSTRAINT fk_seller_user;
ALTER TABLE sellers
   ADD CONSTRAINT fk_seller_user
      FOREIGN KEY ( user_id )
         REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE products DROP CONSTRAINT fk_seller;
ALTER TABLE products MODIFY
   seller_id null;
ALTER TABLE products
   ADD CONSTRAINT fk_seller
      FOREIGN KEY ( seller_id )
         REFERENCES sellers ( seller_id )
            ON DELETE SET NULL;

ALTER TABLE carts DROP CONSTRAINT fk_cart_user;
ALTER TABLE carts
   ADD CONSTRAINT fk_cart_user
      FOREIGN KEY ( user_id )
         REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE cart_items DROP CONSTRAINT fk_cart_item_product;
ALTER TABLE cart_items
   ADD CONSTRAINT fk_cart_item_product
      FOREIGN KEY ( product_id )
         REFERENCES products ( product_id )
            ON DELETE CASCADE;

ALTER TABLE cart_items DROP CONSTRAINT fk_cart_item_cart;
ALTER TABLE cart_items
   ADD CONSTRAINT fk_cart_item_cart
      FOREIGN KEY ( cart_id )
         REFERENCES carts ( cart_id )
            ON DELETE CASCADE;

-- Constraint 이름을 지정하지 않았기 때문에 테이블을 재생성하는 방식으로 처리
DROP TABLE refresh_token;
CREATE TABLE refresh_token (
   refresh_token_id RAW(16) PRIMARY KEY,
   user_id          RAW(16) NOT NULL UNIQUE,
   token            VARCHAR2(512) NOT NULL,
   expiry_date      TIMESTAMP NOT NULL,
   CONSTRAINT fk_refresh_token_user FOREIGN KEY ( user_id )
      REFERENCES users ( user_id )
         ON DELETE CASCADE
);

ALTER TABLE user_a11y_settings DROP CONSTRAINT fk_user_a11y_settings;
ALTER TABLE user_a11y_settings
   ADD CONSTRAINT fk_user_a11y_settings
      FOREIGN KEY ( user_id )
         REFERENCES users ( user_id )
            ON DELETE CASCADE;

DELETE FROM product_ai_summary;
-- ALTER TABLE product_ai_summary DROP CONSTRAINT fk_product_ai_summary_product;
ALTER TABLE product_ai_summary
   ADD CONSTRAINT fk_product_ai_summary_product
      FOREIGN KEY ( product_id )
         REFERENCES products ( product_id )
            ON DELETE CASCADE;

ALTER TABLE product_images DROP CONSTRAINT fk_product;
ALTER TABLE product_images
   ADD CONSTRAINT fk_product
      FOREIGN KEY ( product_id )
         REFERENCES products ( product_id )
            ON DELETE CASCADE;