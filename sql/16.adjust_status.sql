UPDATE addresses
SET receiver_phone = REPLACE(receiver_phone, '-', '')
WHERE LENGTH(receiver_phone) = 13;
COMMIT;

UPDATE orders
SET receiver_phone = REPLACE(receiver_phone, '-', '')
WHERE LENGTH(receiver_phone) = 13;

UPDATE orders
SET receiver_phone = CASE
                         WHEN LENGTH(receiver_phone) = 13 THEN REPLACE(receiver_phone, '-', '')
                         ELSE receiver_phone
    END,
    order_status   = CASE
                         WHEN order_status = 'PAID_PENDING' THEN 'PENDING'
                         ELSE order_status
        END
WHERE (LENGTH(receiver_phone) = 13)
   OR (order_status = 'PAID_PENDING');
COMMIT;

UPDATE sellers
SET business_number = REPLACE(business_number, '-', ''),
    seller_grade    = CASE
                          WHEN seller_grade = 'BRONZE' THEN 'CERTIFIED'
                          WHEN seller_grade = 'SILVER' THEN 'SUPERIORITY'
                          ELSE seller_grade
        END
WHERE (business_number LIKE '%-%')
   OR (seller_grade IN ('BRONZE', 'SILVER'));
COMMIT;