INSERT INTO sellers (
    seller_id,
    user_id,
    seller_name,
    business_number,
    seller_grade,
    seller_intro,
    is_a11y_guarantee,
    seller_submit_status,
    submit_date,
    approved_date,
    updated_at
)
VALUES
    ('019a69f1-f60c-73ab-8d82-9233d471597d', '019a698a-43ea-7785-87a6-4ba7e9e58784', '강철 상점', '111-11-11111', 'NEWER', '강철처럼 튼튼한 제품을 판매합니다.', TRUE, 'APPROVED', current_timestamp, current_timestamp, current_timestamp),
    ('019a69f1-f60c-7e35-9f91-0f41aef5f35f', '019a698a-43ea-7dca-8b34-cde9a3850adb', '친절한 영희네', '222-22-22222', 'REGULAR', '고객님께 항상 친절한 상점입니다.', FALSE, 'APPROVED', current_timestamp, current_timestamp, current_timestamp),
    ('019a69f1-f60c-76ea-9cbd-afb048644daf', '019a698a-43ea-7ba3-8634-d17b296bd88c', '산소탱크 스포츠', '333-33-33333', 'NEWER', '지치지 않는 열정으로 스포츠 용품을 판매합니다.', FALSE, 'APPROVED', current_timestamp, current_timestamp, current_timestamp);
