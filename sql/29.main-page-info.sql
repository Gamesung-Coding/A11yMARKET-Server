CREATE OR REPLACE VIEW view_monthly_popular_products AS
   SELECT p.product_id,
          p.product_name,
          p.product_price,
          pi.image_url AS product_image_url,
          p.category_id,
          cat.category_name,
          p.seller_id,
          SUM(oi.product_quantity) AS monthly_sales_volume,
          COUNT(DISTINCT o.order_id) AS monthly_order_count,
          RANK()
          OVER(
              ORDER BY SUM(oi.product_quantity) DESC
          ) AS ranking
     FROM products p
     JOIN order_items oi
   ON p.product_id = oi.product_id
     JOIN orders o
   ON oi.order_id = o.order_id
     JOIN categories cat
   ON p.category_id = cat.category_id
     LEFT JOIN product_images pi
   ON p.product_id = pi.product_id
    WHERE o.created_at >= trunc(
         sysdate,
         'MM'
      )
      AND o.created_at < add_months(
      trunc(
         sysdate,
         'MM'
      ),
      1
   )
    GROUP BY p.product_id,
             p.product_name,
             p.product_price,
             pi.image_url,
             p.category_id,
             cat.category_name,
             p.seller_id
    ORDER BY monthly_sales_volume DESC;