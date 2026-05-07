CREATE OR REPLACE VIEW view_seller_dashboard_stats AS
   SELECT s.seller_id,
          SUM(
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   oi.product_price * oi.product_quantity
                ELSE
                   0
             END
          ) AS total_revenue,
          COUNT(DISTINCT
             CASE
                WHEN oi.order_item_status != 'REJECTED' THEN
                   oi.order_item_id
             END
          ) AS total_order_count,
          COUNT(
             CASE
                WHEN oi.order_item_status = 'CONFIRMED' THEN
                   1
             END
          ) AS confirmed_count,
          COUNT(
             CASE
                WHEN oi.order_item_status IN('CANCELED',
                                             'RETURNED') THEN
                   1
             END
          ) AS refunded_count
     FROM sellers s
     LEFT JOIN products p
   ON s.seller_id = p.seller_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
    WHERE p.product_status = 'APPROVED'
    GROUP BY s.seller_id;

CREATE OR REPLACE VIEW view_seller_top_products AS
   SELECT p.seller_id,
          p.product_id,
          p.product_name,
          p.product_price,
          pi.image_url AS product_image_url,
          COUNT(oi.order_item_id) AS order_count,
          SUM(oi.product_quantity) AS total_quantity_sold,
          SUM(oi.product_price * oi.product_quantity) AS total_sales_amount,
          RANK()
          OVER(PARTITION BY p.seller_id
               ORDER BY SUM(oi.product_price * oi.product_quantity) DESC
          ) AS sales_rank
     FROM products p
     LEFT JOIN product_images pi
   ON p.product_id = pi.product_id
     LEFT JOIN order_items oi
   ON p.product_id = oi.product_id
      AND oi.order_item_status = 'CONFIRMED'
    WHERE p.product_status = 'APPROVED'
    GROUP BY p.seller_id,
             p.product_id,
             p.product_name,
             p.product_price,
             pi.image_url
   HAVING COUNT(oi.order_item_id) > 0
    ORDER BY p.seller_id,
             sales_rank;

CREATE OR REPLACE VIEW view_monthly_popular_products AS
SELECT p.product_id,
       p.product_name,
       p.product_price,
       (SELECT image_url
        FROM product_images pi
        WHERE pi.product_id = p.product_id
          AND pi.image_sequence = 1) AS product_image_url,
       p.category_id,
       cat.category_name,
       p.seller_id,
       SUM(oi.product_quantity)      AS monthly_sales_volume,
       COUNT(DISTINCT o.order_id)    AS monthly_order_count,
       RANK() OVER (
           ORDER BY SUM(oi.product_quantity) DESC
           )                         AS ranking
FROM products p
         JOIN order_items oi ON p.product_id = oi.product_id
         JOIN orders o ON oi.order_id = o.order_id
         JOIN categories cat ON p.category_id = cat.category_id
WHERE o.created_at >= CURRENT_TIMESTAMP - INTERVAL '1 month'
  AND o.created_at < CURRENT_TIMESTAMP
  AND oi.order_item_status IN ('PAID', 'ACCEPTED', 'SHIPPED', 'CONFIRMED')
  AND p.product_status = 'APPROVED'
GROUP BY p.product_id,
         p.product_name,
         p.product_price,
         p.category_id,
         cat.category_name,
         p.seller_id;

CREATE OR REPLACE VIEW view_category_recommendations AS
WITH RECURSIVE
    category_tree (
                   root_id,
                   root_name,
                   leaf_id
        ) AS (
        SELECT category_id,
               category_name,
               category_id
        FROM categories
        WHERE parent_cat_id IS NULL
        UNION ALL
        SELECT p.root_id,
               p.root_name,
               cat.category_id
        FROM categories cat
                 JOIN category_tree p ON cat.parent_cat_id = p.leaf_id),
    ranked_products AS (SELECT ct.root_id,
                               ct.root_name,
                               vp.product_id,
                               vp.product_name,
                               vp.product_price,
                               vp.product_image_url,
                               vp.monthly_sales_volume,
                               ROW_NUMBER() OVER (
                                   PARTITION BY ct.root_id
                                   ORDER BY vp.monthly_sales_volume DESC, vp.product_id DESC
                                   ) AS rn
                        FROM view_monthly_popular_products vp
                                 JOIN category_tree ct ON vp.category_id = ct.leaf_id)
SELECT *
FROM ranked_products
WHERE rn <= 4;
