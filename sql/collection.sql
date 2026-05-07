DROP VIEW IF EXISTS view_category_recommendations CASCADE;
DROP VIEW IF EXISTS view_monthly_popular_products CASCADE;
DROP VIEW IF EXISTS view_seller_top_products CASCADE;
DROP VIEW IF EXISTS view_seller_dashboard_stats CASCADE;

DROP TABLE IF EXISTS main_page_events CASCADE;
DROP TABLE IF EXISTS refresh_token CASCADE;
DROP TABLE IF EXISTS product_ai_summary CASCADE;
DROP TABLE IF EXISTS user_a11y_profiles CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS carts CASCADE;
DROP TABLE IF EXISTS product_images CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS sellers CASCADE;
DROP TABLE IF EXISTS addresses CASCADE;
DROP TABLE IF EXISTS user_oauth_links CASCADE;
DROP TABLE IF EXISTS users CASCADE;


-- ==========================================
-- File: 01.1.users.sql
-- ==========================================

CREATE TABLE users
(
    user_id       UUID PRIMARY KEY,
    user_name     VARCHAR(30) NOT NULL,
    user_pass     VARCHAR(60),
    user_email    VARCHAR(254) UNIQUE,
    user_phone    VARCHAR(15) UNIQUE,
    user_nickname VARCHAR(100),
    user_role     VARCHAR(30) NOT NULL,
    created_at    TIMESTAMP DEFAULT current_timestamp,
    updated_at    TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT chk_user_contact
        CHECK ( user_email IS NOT NULL
            OR user_phone IS NOT NULL )
);


-- ==========================================
-- File: 01.2.users-dummy.sql
-- ==========================================

INSERT INTO users (user_id,
                   user_name,
                   user_pass,
                   user_email,
                   user_phone,
                   user_nickname,
                   user_role,
                   created_at,
                   updated_at)
VALUES ('019a698a-43ea-7785-87a6-4ba7e9e58784', '김철수', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user1@example.com', '01012345671', '강철개발자', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-7dca-8b34-cde9a3850adb', '이영희', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user2@example.com', '01012345672', '친절한영희씨', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-7ba3-8634-d17b296bd88c', '박지성', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user3@example.com', '01012345673', '산소탱크', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-74eb-8e34-51462055755e', '관리자', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'admin@example.com', '01098765432', '총관리자', 'ADMIN', current_timestamp, current_timestamp),
       ('019a698a-43ea-7843-89bd-8d1b6e9a5cfe', '최민식', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user4@example.com', '01012345674', '연기파배우', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-7f6d-b7d6-b0682abbd378', '유재석', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user5@example.com', '01012345675', '메뚜기', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-75a7-ae59-bcec4f0361ac', '아이유', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user6@example.com', '01012345676', '이지금', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-7e4a-a010-65cd58d2ccd8', '손흥민', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user7@example.com', '01012345677', '캡틴손', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-7b27-9103-a70f0065184a', '김연아', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user8@example.com', '01012345678', '피겨퀸', 'USER', current_timestamp, current_timestamp),
       ('019a698a-43ea-7c95-8b8a-e2ed1625f1f8', '이순신', '$2a$12$OxYBiRRrtePakTIhbVgJr.XzTF6tiAec2GefCb0SPqOTUXB5glRnG',
        'user9@example.com', '01012345679', '성웅', 'USER', current_timestamp, current_timestamp);

COMMIT;

-- ==========================================
-- File: 01.3.user_oauth_links.sql
-- ==========================================

CREATE TABLE user_oauth_links
(
    user_oauth_link_id UUID PRIMARY KEY,
    user_id            UUID,
    oauth_provider     VARCHAR(50)  NOT NULL,
    oauth_provider_id  VARCHAR(255) NOT NULL,
    created_at         TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT fk_user_oauth_links_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);


-- ==========================================
-- File: 02.1.addresses.sql
-- ==========================================

CREATE TABLE addresses
(
    address_id       UUID PRIMARY KEY,
    user_id          UUID                  NOT NULL,
    address_name     VARCHAR(100)          NOT NULL,
    receiver_name    VARCHAR(30)           NOT NULL,
    receiver_phone   VARCHAR(15)           NOT NULL,
    receiver_zipcode VARCHAR(5)            NOT NULL,
    receiver_addr1   VARCHAR(100)          NOT NULL,
    receiver_addr2   VARCHAR(200),
    is_default       BOOLEAN DEFAULT FALSE NOT NULL,
    created_at       TIMESTAMP             NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);


-- ==========================================
-- File: 02.2.addresses-dummy.sql
-- ==========================================

INSERT INTO addresses (address_id,
                       user_id,
                       address_name,
                       receiver_name,
                       receiver_phone,
                       receiver_zipcode,
                       receiver_addr1,
                       receiver_addr2,
                       is_default,
                       created_at)
VALUES ('019a698d-82c0-7c66-ac4a-293c84acfa52', '019a698a-43ea-7785-87a6-4ba7e9e58784', '집', '김철수', '010-1234-5671',
        '12345', '서울시 강남구 테헤란로 1', '101동 101호', TRUE, current_timestamp),
       ('019a698d-82c0-7499-b33d-51a99e798e68', '019a698a-43ea-7dca-8b34-cde9a3850adb', '집', '이영희', '010-1234-5672',
        '23456', '서울시 서초구 반포대로 2', '202동 202호', TRUE, current_timestamp),
       ('019a698d-82c0-79b1-907a-cd71118370aa', '019a698a-43ea-7ba3-8634-d17b296bd88c', '집', '박지성', '010-1234-5673',
        '34567', '경기도 수원시 영통구 3', '303동 303호', TRUE, current_timestamp),
       ('019a698d-82c0-76f9-a7d0-bf5219a3dd1e', '019a698a-43ea-7ba3-8634-d17b296bd88c', '박지성 사무실', '박지성 (사무실)',
        '010-9876-5432', '45678', '서울시 중구 세종대로 4', NULL, FALSE, current_timestamp),
       ('019a698d-82c0-70e9-9f2a-30bbf500e1ab', '019a698a-43ea-7843-89bd-8d1b6e9a5cfe', '집', '최민식', '010-1234-5674',
        '56789', '부산시 해운대구 5', '505동 505호', TRUE, current_timestamp),
       ('019a698d-82c0-77d5-aa4f-70751fece6d7', '019a698a-43ea-7f6d-b7d6-b0682abbd378', '집', '유재석', '010-1234-5675',
        '67890', '서울시 마포구 상암동 6', '606동 606호', TRUE, current_timestamp),
       ('019a698d-82c0-7d54-9e27-2b0e0ae113ab', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '집', '아이유', '010-1234-5676',
        '78901', '서울시 강남구 청담동 7', '707동 707호', TRUE, current_timestamp),
       ('019a698d-82c0-7ab0-b452-942f32cfa5dc', '019a698a-43ea-7e4a-a010-65cd58d2ccd8', '집', '손흥민', '010-1234-5677',
        '89012', '강원도 춘천시 8', '808동 808호', TRUE, current_timestamp),
       ('019a698d-82c0-7e6b-b1a3-16575c02472a', '019a698a-43ea-7b27-9103-a70f0065184a', '집', '김연아', '010-1234-5678',
        '90123', '경기도 군포시 9', '909동 909호', TRUE, current_timestamp),
       ('019a698d-82c0-7ebc-8d29-9ee1144b2660', '019a698a-43ea-7c95-8b8a-e2ed1625f1f8', '집', '이순신', '010-1234-5679',
        '11234', '전라남도 여수시 10', '1010동 1010호', TRUE, current_timestamp),
       ('019a6994-08e0-7ef0-94f2-97c04602190b', '019a698a-43ea-7785-87a6-4ba7e9e58784', '회사', '김철수 (회사)',
        '010-1111-1111', '54321', '서울시 강남구 테헤란로 200', '강남파이낸스센터', FALSE, current_timestamp),
       ('019a6994-08e0-7d02-8ff1-3b65c56ff860', '019a698a-43ea-7785-87a6-4ba7e9e58784', '본가', '김철수 (본가)',
        '010-2222-2222', '54322', '경기도 성남시 분당구', '101동 202호', FALSE, current_timestamp),
       ('019a6994-08e0-72f9-bd40-1915f7ce6cb4', '019a698a-43ea-7dca-8b34-cde9a3850adb', '사무실', '이영희 (사무실)',
        '010-3333-3333', '65432', '서울시 서초구 서초대로 300', '사무실 5층', FALSE, current_timestamp),
       ('019a6994-08e0-7101-9876-72d4f407929b', '019a698a-43ea-7f6d-b7d6-b0682abbd378', '유재석 물류센터', '유재석 (물류센터)',
        '010-4444-4444', '76543', '경기도 이천시 물류단지', 'A동 101', FALSE, current_timestamp),
       ('019a6994-08e0-7200-8e5e-5648644fbac7', '019a698a-43ea-7ba3-8634-d17b296bd88c', '박지성 본사', '박지성 (본사)',
        '010-5555-5555', '87654', '서울시 종로구 세종대로 100', '광화문빌딩', FALSE, current_timestamp),
       ('019a6994-08e0-75b8-bf43-b2def45efab4', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '작업실', '아이유 (작업실)',
        '010-6666-6666', '98765', '서울시 성동구 성수동', '스튜디오 301호', FALSE, current_timestamp),
       ('019a6994-08e0-7625-8607-c2e61dc20319', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '본가', '아이유 (본가)',
        '010-7777-7777', '12121', '경기도 과천시', '주택', FALSE, current_timestamp),
       ('019a6994-08e0-7444-bedc-c3cb3f43ecdf', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '제주도 별장', '이지은 (실명배송)',
        '010-8888-8888', '34343', '제주도 제주시 애월읍', '별장', FALSE, current_timestamp),
       ('019a6994-08e0-7d14-b14b-bb68e835a6f1', '019a698a-43ea-7c95-8b8a-e2ed1625f1f8', '거제도 숙소', '이순신 (거제도)',
        '010-9999-9999', '45454', '경상남도 거제시', '조선소 근처', FALSE, current_timestamp),
       ('019a6994-08e0-7110-80d1-5a0125517b21', '019a698a-43ea-7c95-8b8a-e2ed1625f1f8', '서울 숙소', '이순신 (서울숙소)',
        '010-0000-0000', '56565', '서울시 동작구 노량진동', '오피스텔', FALSE, current_timestamp);


-- ==========================================
-- File: 03.1.seller.sql
-- ==========================================

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


-- ==========================================
-- File: 03.2.seller-dummy.sql
-- ==========================================

INSERT INTO sellers (seller_id,
                     user_id,
                     seller_name,
                     business_number,
                     seller_grade,
                     seller_intro,
                     is_a11y_guarantee,
                     seller_submit_status,
                     submit_date,
                     approved_date,
                     updated_at)
VALUES ('019a69f1-f60c-73ab-8d82-9233d471597d', '019a698a-43ea-7785-87a6-4ba7e9e58784', '강철 상점', '111-11-11111',
        'NEWER', '강철처럼 튼튼한 제품을 판매합니다.', TRUE, 'APPROVED', current_timestamp, current_timestamp, current_timestamp),
       ('019a69f1-f60c-7e35-9f91-0f41aef5f35f', '019a698a-43ea-7dca-8b34-cde9a3850adb', '친절한 영희네', '222-22-22222',
        'REGULAR', '고객님께 항상 친절한 상점입니다.', FALSE, 'APPROVED', current_timestamp, current_timestamp, current_timestamp),
       ('019a69f1-f60c-76ea-9cbd-afb048644daf', '019a698a-43ea-7ba3-8634-d17b296bd88c', '산소탱크 스포츠', '333-33-33333',
        'NEWER', '지치지 않는 열정으로 스포츠 용품을 판매합니다.', FALSE, 'APPROVED', current_timestamp, current_timestamp,
        current_timestamp);


-- ==========================================
-- File: 04.1.categories.sql
-- ==========================================

CREATE TABLE categories
(
    category_id   UUID PRIMARY KEY,
    parent_cat_id UUID,
    category_name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_parent_cat FOREIGN KEY (parent_cat_id)
        REFERENCES categories (category_id)
);


-- ==========================================
-- File: 04.2.categories-dummy.sql
-- ==========================================

INSERT INTO categories (category_id,
                        parent_cat_id,
                        category_name)
VALUES ('019a69f3-b7b4-74b4-902d-651d1f11d323', NULL, '디지털/가전'),
       ('019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', NULL, '패션/의류'),
       ('019a69f3-b7b4-7253-8447-2516faee63f4', NULL, '스포츠/레저'),
       ('019a69f3-b7b4-79cd-a461-61ee136c6801', '019a69f3-b7b4-74b4-902d-651d1f11d323', '노트북'),
       ('019a69f3-b7b4-7a8f-ba32-343d5068fae9', '019a69f3-b7b4-74b4-902d-651d1f11d323', '모바일'),
       ('019a69f3-b7b4-71ae-9dff-aa18f0005106', '019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', '남성의류'),
       ('019a69f3-b7b4-743b-b816-e3a6973b47d1', '019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', '여성의류'),
       ('019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', '019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', '패션잡화'),
       ('019a69f3-b7b4-76b3-b1de-1994732363db', '019a69f3-b7b4-7253-8447-2516faee63f4', '캠핑용품'),
       ('019a69f3-b7b4-7e4e-83b7-394018b33917', '019a69f3-b7b4-7253-8447-2516faee63f4', '축구용품');


-- ==========================================
-- File: 05.1.products.sql
-- ==========================================

CREATE TABLE products
(
    product_id          UUID PRIMARY KEY,
    seller_id           UUID,
    category_id         UUID                                NOT NULL,
    product_price       INT                                 NOT NULL,
    product_stock       INT                                 NOT NULL,
    product_name        VARCHAR(256)                        NOT NULL,
    product_description TEXT                                NOT NULL,
    product_status      VARCHAR(20)                         NOT NULL,
    submit_date         TIMESTAMP DEFAULT current_timestamp NOT NULL,
    approved_date       TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT current_timestamp NOT NULL,
    CONSTRAINT fk_seller FOREIGN KEY (seller_id)
        REFERENCES sellers (seller_id) ON DELETE SET NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES categories (category_id)
);


-- ==========================================
-- File: 05.2.products-dummy.sql
-- ==========================================

INSERT INTO products (product_id,
                      seller_id,
                      category_id,
                      product_price,
                      product_stock,
                      product_name,
                      product_description,
                      product_status,
                      approved_date)
VALUES ('019a69f5-c3c0-7412-bc20-8e2345ea7203', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 1490000, 50, '강철 게이밍 노트북 15인치',
        '최신 9세대 CPU와 고성능 그래픽카드를 탑재한 게이밍 노트북입니다.', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-78fe-aff4-e093db2e9ae1', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 890000, 100, '강철 사무용 노트북 13인치',
        '가벼운 무게와 오래 가는 배터리로 사무용, 학생용으로 최적화된 노트북입니다.', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7212-9588-d633562e520c', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 2100000, 30, '강철 크리에이터 노트북 16인치',
        '4K OLED 디스플레이와 외장 GPU로 영상 편집 및 디자인 작업에 특화된 전문가용 노트북입니다.', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7a71-82ae-a2715abb802a', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 750000, 80, '강철 학생용 노트북 14인치', '인강 및 문서 작업에 충분한 성능을 갖춘 가성비 학생용 노트북입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-74a6-adb5-774f39fc7fc7', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 1190000, 40, '강철 울트라북 14인치',
        '1kg 미만의 초경량 무게와 세련된 디자인을 갖춘 프리미엄 울트라북입니다.', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-77c2-bbca-24506b7a07c1', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 1800000, 20, '강철 2-in-1 노트북 13인치',
        '터치스크린과 360도 회전 힌지로 태블릿처럼 사용 가능한 노트북입니다.', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7d26-98c2-e7e60f725c16', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-79cd-a461-61ee136c6801', 450000, 150, '강철 크롬북 11인치', '웹 서핑과 클라우드 작업에 최적화된 저렴한 크롬북입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7646-b57e-f56d0a09cd97', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 1200000, 200, '강철 스마트폰 V1', 'AI 카메라와 초고속 프로세서를 탑재한 최신 플래그십 스마트폰.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7bff-bf72-689179eaa8d9', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 550000, 300, '강철 스마트폰 A1 (보급형)', '대화면과 대용량 배터리를 탑재한 가성비 스마트폰.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7565-aa94-5c6e5d0b90f4', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 880000, 150, '강철 스마트폰 V1 (Mini)', 'V1의 성능은 그대로, 한 손에 잡히는 컴팩트 사이즈 스마트폰.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7ac5-88b8-00c2d38b7dfe', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 650000, 100, '강철 태블릿 10인치', '영상 시청 및 필기에 최적화된 10인치 태블릿.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-7158-8ed7-24148e46011d', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 990000, 80, '강철 태블릿 12인치 (Pro)', '전문가용 드로잉 및 작업이 가능한 고성능 12인치 프로 태블릿.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-76ba-a0fd-3a121b5bae04', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 199000, 250, '강철 무선 이어폰', '노이즈 캔슬링을 지원하는 고음질 무선 이어폰.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-7f83-bcef-1a508d05263f', '019a69f1-f60c-73ab-8d82-9233d471597d',
        '019a69f3-b7b4-7a8f-ba32-343d5068fae9', 100000, 50, '스마트폰 보조배터리 20000mAh', '고속 충전을 지원하는 20000mAh 대용량 보조배터리.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7d83-8b89-3fb3ed04228d', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 59000, 100, '영희네 남성 옥스포드 셔츠', '깔끔한 디자인의 화이트 옥스포드 셔츠입니다. (사이즈: L)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7d7a-a40b-c8bc7dd56c27', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 89000, 120, '영희네 남성 슬랙스 (블랙)', '신축성이 좋은 편안한 착용감의 블랙 슬랙스입니다. (사이즈: 32)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-725c-85ed-f652b98f9cfb', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 35000, 200, '영희네 남성 반팔 티셔츠 (네이비)',
        '부드러운 면 소재의 기본 네이비 반팔 티셔츠입니다. (사이즈: XL)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-710b-8a31-6c1554761879', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 120000, 70, '영희네 남성 경량 패딩 조끼', '간절기에 입기 좋은 가벼운 남성용 패딩 조끼입니다. (색상: 그레이)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7668-b27c-62db89013277', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 65000, 100, '영희네 남성 트레이닝 팬츠', '활동성이 편한 조거 스타일의 트레이닝 팬츠입니다. (색상: 차콜)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-77a9-99e8-7c5c9545244a', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 150000, 50, '영희네 남성 울 코트 (싱글)', '겨울용 따뜻한 울 소재의 싱글 코트입니다. (색상: 블랙)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7ad8-9e8e-0c415c9f544c', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-71ae-9dff-aa18f0005106', 45000, 130, '영희네 남성 후드 티셔츠 (그레이)',
        '기모 안감으로 따뜻한 베이직 후드 티셔츠입니다. (사이즈: L)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7caa-a08e-69d8ce1dd7cf', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 79000, 150, '영희네 여성 쉬폰 원피스', '봄 신상 하늘하늘한 쉬폰 롱 원피스입니다. (색상: 핑크)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7a93-8327-609bd7a9752b', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 99000, 100, '영희네 여성 트위드 자켓', '격식 있는 자리에 어울리는 클래식 트위드 자켓입니다. (색상: 아이보리)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7a51-8834-4e08a111fdb1', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 55000, 130, '영희네 여성 와이드 슬랙스 (베이지)',
        '편안하고 스타일리시한 와이드 핏 여성 슬랙스입니다. (사이즈: M)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-710a-9700-a3e8d43cf1ee', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 42000, 180, '영희네 여성 블라우스 (스카이블루)',
        '오피스룩으로 입기 좋은 부드러운 소재의 블라우스입니다. (사이즈: 55)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-764c-87e0-c37dff3f8891', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 88000, 90, '영희네 여성 데님 스커트 (롱)', 'A라인으로 퍼지는 청순한 스타일의 롱 데님 스커트입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-76ca-8a97-2fe1fc785328', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 130000, 60, '영희네 여성 트렌치 코트 (베이지)', '가을 필수 아이템, 클래식 디자인의 여성 트렌치 코트입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-732f-b9bd-2a7ab2f35ba3', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-743b-b816-e3a6973b47d1', 29000, 200, '영희네 여성 기본 가디건 (블랙)',
        '어디에나 걸치기 좋은 기본 블랙 가디건입니다. (사이즈: Free)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-743a-b124-096280c2cfa6', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 120000, 80, '영희네 천연 소가죽 벨트 (브라운)', '클래식한 디자인의 천연 소가죽 남성 벨트입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-73f8-a848-9378f3c1af02', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 180000, 70, '영희네 캔버스 크로스백 (아이보리)', '가볍고 수납이 용이한 아이보리 캔버스 크로스백입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7673-a6d5-34699d4b2d60', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 45000, 150, '영희네 볼캡 (베이지)', '심플한 로고 디자인의 베이직 베이지 볼캡입니다.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-77c3-9c02-6eb5d8e34797', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 210000, 60, '영희네 여성 장지갑 (레드)', '고급스러운 가죽 소재의 여성용 레드 장지갑입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7c23-a7dd-0c541cb65a52', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 78000, 100, '영희네 실크 스카프 (블루)', '부드러운 100% 실크 소재의 패턴 스카프입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7d70-83ff-88279e50ec05', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 15000, 300, '영희네 패션 양말 5종 세트', '다양한 디자인의 패션 양말 5종 세트입니다.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-7a93-b5ad-d7ae6588c162', '019a69f1-f60c-7e35-9f91-0f41aef5f35f',
        '019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', 99000, 80, '영희네 선글라스 (블랙)', 'UV 차단 기능이 있는 베이직 블랙 선글라스입니다.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-7cf2-9f8b-69058c4e4b1c', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 250000, 30, '산소탱크 원터치 텐트 (4인용)', '설치가 간편한 4인용 방수 원터치 텐트입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-70a0-8693-306c227d5cbe', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 80000, 50, '산소탱크 캠핑 의자 (릴렉스 체어)',
        '편안하게 기댈 수 있는 릴렉스 캠핑 의자입니다. (색상: 네이비)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-71a9-871f-cacd8633c8bb', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 120000, 40, '산소탱크 캠핑 테이블 (접이식)', '알루미늄 소재의 가벼운 접이식 캠핑 테이블입니다. (사이즈: L)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7bf8-9bd3-0b7eda79b84b', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 95000, 60, '산소탱크 캠핑용 침낭 (사계절)', '사계절 사용 가능한 고성능 캠핑용 침낭입니다.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-7959-a603-f710fd030402', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 65000, 70, '산소탱크 캠핑용 랜턴 (LED)', '밝기 조절이 가능한 충전식 LED 캠핑 랜턴입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-782c-9ca5-5fbf8fc7e750', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 150000, 30, '산소탱크 캠핑용 화로대', '불멍과 바베큐가 가능한 스테인레스 화로대입니다.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-76c0-8012-0ccafdbcd586', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 180000, 25, '산소탱크 아이스박스 50L', '보냉력이 뛰어난 50L 대용량 아이스박스입니다.', 'APPROVED',
        current_timestamp),
       ('019a69f5-c3c0-71ab-8ec9-0ab9db76a800', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-76b3-b1de-1994732363db', 45000, 100, '산소탱크 캠핑용 코펠 (4인용)', '경질 알루미늄 소재의 4인용 코펠 세트입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7129-94ed-97c51816d0a2', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 150000, 50, '산소탱크 축구공 (K-리그 공인구)', '프로 경기용 K-리그 공인구입니다. (5호)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7fa5-932a-b1b9515efa64', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 180000, 40, '산소탱크 축구화 (FG 스터드)', '천연 잔디용 FG 스터드 축구화입니다. (사이즈: 270mm)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-769f-b19f-89840437d235', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 160000, 45, '산소탱크 축구화 (TF 스터드)', '인조 잔디용 TF 스터드 풋살화입니다. (사이즈: 270mm)',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-76d2-9b87-cd61fd7c9189', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 75000, 80, '산소탱크 골키퍼 장갑 (프로용)',
        '그립감이 뛰어난 프로페셔널 등급의 골키퍼 장갑입니다. (사이즈: 9호)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7e5f-98d8-8b4833f6c54a', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 45000, 100, '산소탱크 축구 유니폼 (레드)',
        '땀 배출이 용이한 기능성 축구 유니폼 상하의 세트입니다. (사이즈: L)', 'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7e18-bea1-e5194a9ab10e', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 35000, 120, '산소탱크 스타킹 (블랙)', '미끄럼 방지 패드가 부착된 논슬립 축구 스타킹입니다.',
        'APPROVED', current_timestamp),
       ('019a69f5-c3c0-7cdb-98e6-0b09a59e0f72', '019a69f1-f60c-76ea-9cbd-afb048644daf',
        '019a69f3-b7b4-7e4e-83b7-394018b33917', 25000, 150, '산소탱크 정강이 보호대', '충격 흡수가 뛰어난 경량 정강이 보호대입니다. (사이즈: M)',
        'APPROVED', current_timestamp);


-- ==========================================
-- File: 06.1.product_images.sql
-- ==========================================

CREATE TABLE product_images
(
    image_id       UUID PRIMARY KEY,
    product_id     UUID                NOT NULL,
    image_url      VARCHAR(2048)       NOT NULL,
    alt_text       TEXT,
    created_at     TIMESTAMP DEFAULT current_timestamp,
    image_sequence INT       DEFAULT 1 NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id)
        REFERENCES products (product_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_product_image_sequence ON
    product_images (product_id, image_sequence);


-- ==========================================
-- File: 06.2.product_images-dummy.sql
-- ==========================================

INSERT INTO product_images (image_id,
                            product_id,
                            image_url,
                            alt_text,
                            created_at)
VALUES ('019a69fb-7a5a-76b6-af6f-1b824ea5b9f0', '019a69f5-c3c0-7412-bc20-8e2345ea7203',
        'https://minio.bluenyang.kr/test-images/img%2F1001.jpg', '강철 게이밍 노트북 15인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-7dcf-b097-30d3aa8f2f02', '019a69f5-c3c0-78fe-aff4-e093db2e9ae1',
        'https://minio.bluenyang.kr/test-images/img%2F1002.jpg', '강철 사무용 노트북 13인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-727c-80cc-fb317f5a0dab', '019a69f5-c3c0-7212-9588-d633562e520c',
        'https://minio.bluenyang.kr/test-images/img%2F1003.jpg', '강철 크리에이터 노트북 16인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-7d19-966f-4e2a7d6552c2', '019a69f5-c3c0-7a71-82ae-a2715abb802a',
        'https://minio.bluenyang.kr/test-images/img%2F1004.jpg', '강철 학생용 노트북 14인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-7b9d-bfad-fc2c1b6d2d8b', '019a69f5-c3c0-74a6-adb5-774f39fc7fc7',
        'https://minio.bluenyang.kr/test-images/img%2F1005.jpg', '강철 울트라북 14인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-7a70-962c-9e6b7c4c0b35', '019a69f5-c3c0-77c2-bbca-24506b7a07c1',
        'https://minio.bluenyang.kr/test-images/img%2F1006.jpg', '강철 2-in-1 노트북 13인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-75b0-8b41-801e2cef4f3c', '019a69f5-c3c0-7d26-98c2-e7e60f725c16',
        'https://minio.bluenyang.kr/test-images/img%2F1007.jpg', '강철 크롬북 11인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-72a4-b4d3-5fc2fc6b01ae', '019a69f5-c3c0-7646-b57e-f56d0a09cd97',
        'https://minio.bluenyang.kr/test-images/img%2F1008.jpg', '강철 스마트폰 V1 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-7554-b9f8-bff75879fe9e', '019a69f5-c3c0-7bff-bf72-689179eaa8d9',
        'https://minio.bluenyang.kr/test-images/img%2F1009.jpg', '강철 스마트폰 A1 (보급형) 대표 이미지', current_timestamp),
       ('019a69fb-7a5a-796d-867c-081840793315', '019a69f5-c3c0-7565-aa94-5c6e5d0b90f4',
        'https://minio.bluenyang.kr/test-images/img%2F1010.jpg', '강철 스마트폰 V1 (Mini) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-748e-97d5-8f84c2657432', '019a69f5-c3c0-7ac5-88b8-00c2d38b7dfe',
        'https://minio.bluenyang.kr/test-images/img%2F1011.jpg', '강철 태블릿 10인치 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-718e-be79-17b16430156f', '019a69f5-c3c0-7158-8ed7-24148e46011d',
        'https://minio.bluenyang.kr/test-images/img%2F1012.jpg', '강철 태블릿 12인치 (Pro) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7f48-bcde-4a2de95cd414', '019a69f5-c3c0-76ba-a0fd-3a121b5bae04',
        'https://minio.bluenyang.kr/test-images/img%2F1013.jpg', '강철 무선 이어폰 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7955-a7a1-9585ff96d1e2', '019a69f5-c3c0-7f83-bcef-1a508d05263f',
        'https://minio.bluenyang.kr/test-images/img%2F1014.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-765e-9b6c-fd521018db29', '019a69f5-c3c0-7d83-8b89-3fb3ed04228d',
        'https://minio.bluenyang.kr/test-images/img%2F1015.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-7c02-b064-fe849dfa57a7', '019a69f5-c3c0-7d7a-a40b-c8bc7dd56c27',
        'https://minio.bluenyang.kr/test-images/img%2F1001.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-751b-b03f-b390d7a71add', '019a69f5-c3c0-725c-85ed-f652b98f9cfb',
        'https://minio.bluenyang.kr/test-images/img%2F1002.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-7672-8c9d-617dd9dc14ba', '019a69f5-c3c0-710b-8a31-6c1554761879',
        'https://minio.bluenyang.kr/test-images/img%2F1003.jpg', '영희네 남성 경량 패딩 조끼 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-72bb-874b-1205e1c335fd', '019a69f5-c3c0-7668-b27c-62db89013277',
        'https://minio.bluenyang.kr/test-images/img%2F1004.jpg', '영희네 남성 트레이닝 팬츠 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7950-99d3-8bc1574bc755', '019a69f5-c3c0-77a9-99e8-7c5c9545244a',
        'https://minio.bluenyang.kr/test-images/img%2F1005.jpg', '영희네 남성 울 코트 (싱글) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7342-8855-2d29dad0c0ad', '019a69f5-c3c0-7ad8-9e8e-0c415c9f544c',
        'https://minio.bluenyang.kr/test-images/img%2F1006.jpg', '영희네 남성 후드 티셔츠 (그레이) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7c9b-bd03-fceea0a086b2', '019a69f5-c3c0-7caa-a08e-69d8ce1dd7cf',
        'https://minio.bluenyang.kr/test-images/img%2F1007.jpg', '영희네 여성 쉬폰 원피스 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7872-9af1-8974faa7fd48', '019a69f5-c3c0-7a93-8327-609bd7a9752b',
        'https://minio.bluenyang.kr/test-images/img%2F1008.jpg', '영희네 여성 트위드 자켓 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7f07-9d42-8ddac6c8bd82', '019a69f5-c3c0-7a51-8834-4e08a111fdb1',
        'https://minio.bluenyang.kr/test-images/img%2F1009.jpg', '영희네 여성 와이드 슬랙스 (베이지) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-742d-9788-3ca2efe21b86', '019a69f5-c3c0-710a-9700-a3e8d43cf1ee',
        'https://minio.bluenyang.kr/test-images/img%2F1010.jpg', '영희네 여성 블라우스 (스카이블루) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7b95-b048-c5c0cea7d170', '019a69f5-c3c0-764c-87e0-c37dff3f8891',
        'https://minio.bluenyang.kr/test-images/img%2F1011.jpg', '영희네 여성 데님 스커트 (롱) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7205-bce0-22f5a1b31bf2', '019a69f5-c3c0-76ca-8a97-2fe1fc785328',
        'https://minio.bluenyang.kr/test-images/img%2F1012.jpg', '영희네 여성 트렌치 코트 (베이지) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7ba5-9479-7c17b024cba0', '019a69f5-c3c0-732f-b9bd-2a7ab2f35ba3',
        'https://minio.bluenyang.kr/test-images/img%2F1013.jpg', '영희네 여성 기본 가디건 (블랙) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7753-a4e8-e9bfd98ff1af', '019a69f5-c3c0-743a-b124-096280c2cfa6',
        'https://minio.bluenyang.kr/test-images/img%2F1014.jpg', '영희네 천연 소가죽 벨트 (브라운) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7f9d-8c4a-142b3a05925d', '019a69f5-c3c0-73f8-a848-9378f3c1af02',
        'https://minio.bluenyang.kr/test-images/img%2F1015.jpg', '영희네 캔버스 크로스백 (아이보리) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7413-b082-ded3a69d688b', '019a69f5-c3c0-7673-a6d5-34699d4b2d60',
        'https://minio.bluenyang.kr/test-images/img%2F1001.jpg', '영희네 볼캡 (베이지) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7502-85ab-1023a36679c6', '019a69f5-c3c0-77c3-9c02-6eb5d8e34797',
        'https://minio.bluenyang.kr/test-images/img%2F1002.jpg', '영희네 여성 장지갑 (레드) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7d16-940a-54ad9f8251ef', '019a69f5-c3c0-7c23-a7dd-0c541cb65a52',
        'https://minio.bluenyang.kr/test-images/img%2F1003.jpg', '영희네 실크 스카프 (블루) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-70d9-b82c-bda82656c2fc', '019a69f5-c3c0-7d70-83ff-88279e50ec05',
        'https://minio.bluenyang.kr/test-images/img%2F1004.jpg', '영희네 패션 양말 5종 세트 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7d06-a4d2-665904bb00b7', '019a69f5-c3c0-7a93-b5ad-d7ae6588c162',
        'https://minio.bluenyang.kr/test-images/img%2F1005.jpg', '영희네 선글라스 (블랙) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7050-b59a-e7b3e4920ac7', '019a69f5-c3c0-7cf2-9f8b-69058c4e4b1c',
        'https://minio.bluenyang.kr/test-images/img%2F1006.jpg', '산소탱크 원터치 텐트 (4인용) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7f27-bf61-4eaf05b31f55', '019a69f5-c3c0-70a0-8693-306c227d5cbe',
        'https://minio.bluenyang.kr/test-images/img%2F1007.jpg', '산소탱크 캠핑 의자 (릴렉스 체어) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-77da-90e8-da59b0ba033e', '019a69f5-c3c0-71a9-871f-cacd8633c8bb',
        'https://minio.bluenyang.kr/test-images/img%2F1008.jpg', '산소탱크 캠핑 테이블 (접이식) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-769f-933e-1169063086e8', '019a69f5-c3c0-7bf8-9bd3-0b7eda79b84b',
        'https://minio.bluenyang.kr/test-images/img%2F1009.jpg', '산소탱크 캠핑용 침낭 (사계절) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7d3b-909d-3aef1f818ae9', '019a69f5-c3c0-7959-a603-f710fd030402',
        'https://minio.bluenyang.kr/test-images/img%2F1010.jpg', '산소탱크 캠핑용 랜턴 (LED) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7e66-b96b-daf30f609278', '019a69f5-c3c0-782c-9ca5-5fbf8fc7e750',
        'https://minio.bluenyang.kr/test-images/img%2F1011.jpg', '산소탱크 캠핑용 화로대 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7424-8be8-f6a9a0a101e2', '019a69f5-c3c0-76c0-8012-0ccafdbcd586',
        'https://minio.bluenyang.kr/test-images/img%2F1012.jpg', '산소탱크 아이스박스 50L 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7877-9c69-32d9c80c06f4', '019a69f5-c3c0-71ab-8ec9-0ab9db76a800',
        'https://minio.bluenyang.kr/test-images/img%2F1013.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-7fcd-9442-f6cd3b79c1de', '019a69f5-c3c0-7129-94ed-97c51816d0a2',
        'https://minio.bluenyang.kr/test-images/img%2F1014.jpg', '산소탱크 축구공 (K-리그 공인구) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-71c8-8e99-2b3cfbc3bfad', '019a69f5-c3c0-7fa5-932a-b1b9515efa64',
        'https://minio.bluenyang.kr/test-images/img%2F1015.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-7f0c-a094-ad1c9b9c227b', '019a69f5-c3c0-769f-b19f-89840437d235',
        'https://minio.bluenyang.kr/test-images/img%2F1001.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-79d9-a491-dae792dc06e2', '019a69f5-c3c0-76d2-9b87-cd61fd7c9189',
        'https://minio.bluenyang.kr/test-images/img%2F1002.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-7161-8f85-8cc502d5c8f6', '019a69f5-c3c0-7e5f-98d8-8b4833f6c54a',
        'https://minio.bluenyang.kr/test-images/img%2F1003.jpg', NULL, current_timestamp),
       ('019a69fb-7a5b-7df6-b34a-95b1281a7b82', '019a69f5-c3c0-7e18-bea1-e5194a9ab10e',
        'https://minio.bluenyang.kr/test-images/img%2F1004.jpg', '산소탱크 스타킹 (블랙) 대표 이미지', current_timestamp),
       ('019a69fb-7a5b-7b77-8ce1-06e150985ad7', '019a69f5-c3c0-7cdb-98e6-0b09a59e0f72',
        'https://minio.bluenyang.kr/test-images/img%2F1005.jpg', '산소탱크 정강이 보호대 대표 이미지', current_timestamp);


-- ==========================================
-- File: 07.1.carts.sql
-- ==========================================

CREATE TABLE carts
(
    cart_id UUID PRIMARY KEY NOT NULL,
    user_id UUID             NOT NULL,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);


-- ==========================================
-- File: 07.2.carts-dummy.sql
-- ==========================================

INSERT INTO carts (cart_id,
                   user_id)
VALUES ('019a6a19-d212-7423-b720-6e30165850bb', '019a698a-43ea-7785-87a6-4ba7e9e58784'),
       ('019a6a19-d212-7faf-85b9-f3fbefd78ef3', '019a698a-43ea-7dca-8b34-cde9a3850adb'),
       ('019a6a19-d212-759d-9d27-22b79510379f', '019a698a-43ea-7ba3-8634-d17b296bd88c'),
       ('019a6a19-d212-77d0-b1e9-e0345d18a2d8', '019a698a-43ea-7843-89bd-8d1b6e9a5cfe'),
       ('019a6a19-d212-7748-b173-b5d47a2173b2', '019a698a-43ea-7f6d-b7d6-b0682abbd378');


-- ==========================================
-- File: 08.1.cart_items.sql
-- ==========================================

CREATE TABLE cart_items
(
    cart_item_id UUID PRIMARY KEY,
    product_id   UUID NOT NULL,
    cart_id      UUID NOT NULL,
    quantity     INT  NOT NULL,
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id)
        REFERENCES products (product_id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id)
        REFERENCES carts (cart_id) ON DELETE CASCADE
);


-- ==========================================
-- File: 08.2.cart_items-dummy.sql
-- ==========================================

INSERT INTO cart_items (cart_item_id,
                        product_id,
                        cart_id,
                        quantity)
VALUES ('019a6a1b-7533-71dc-ab13-625f44b2b822', '019a69f5-c3c0-7412-bc20-8e2345ea7203',
        '019a6a19-d212-7423-b720-6e30165850bb', 1),
       ('019a6a1b-7533-78a8-a61a-4fcbfd15c717', '019a69f5-c3c0-7646-b57e-f56d0a09cd97',
        '019a6a19-d212-7423-b720-6e30165850bb', 2),
       ('019a6a1b-7533-733e-a131-5f11e19864ea', '019a69f5-c3c0-7d83-8b89-3fb3ed04228d',
        '019a6a19-d212-7faf-85b9-f3fbefd78ef3', 1),
       ('019a6a1b-7533-72e4-b966-06aaa7e0cce6', '019a69f5-c3c0-7caa-a08e-69d8ce1dd7cf',
        '019a6a19-d212-7faf-85b9-f3fbefd78ef3', 1),
       ('019a6a1b-7533-708f-b393-bfa2bb4677d7', '019a69f5-c3c0-7cf2-9f8b-69058c4e4b1c',
        '019a6a19-d212-759d-9d27-22b79510379f', 1),
       ('019a6a1b-7533-7937-997c-a8a6d375c9b1', '019a69f5-c3c0-7129-94ed-97c51816d0a2',
        '019a6a19-d212-759d-9d27-22b79510379f', 3),
       ('019a6a1b-7533-7ac5-9ab4-2554ca935036', '019a69f5-c3c0-78fe-aff4-e093db2e9ae1',
        '019a6a19-d212-77d0-b1e9-e0345d18a2d8', 1),
       ('019a6a1b-7533-7536-aabb-7c5b8a2ebb0a', '019a69f5-c3c0-7d7a-a40b-c8bc7dd56c27',
        '019a6a19-d212-7748-b173-b5d47a2173b2', 1),
       ('019a6a1b-7533-73b8-ae1d-5935f460cc78', '019a69f5-c3c0-70a0-8693-306c227d5cbe',
        '019a6a19-d212-7748-b173-b5d47a2173b2', 2);


-- ==========================================
-- File: 09.1.orders.sql
-- ==========================================

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


-- ==========================================
-- File: 09.2.orders-dummy.sql
-- ==========================================

INSERT INTO orders (order_id,
                    user_id,
                    user_name,
                    user_email,
                    user_phone,
                    receiver_name,
                    receiver_phone,
                    receiver_zipcode,
                    receiver_addr1,
                    receiver_addr2,
                    total_price,
                    payment_key)
VALUES ('019a69ef-1209-70ef-8f92-9678c20cf1f0', '019a698a-43ea-7785-87a6-4ba7e9e58784', '김철수', 'user1@example.com',
        '01012345671', '김철수', '010-1234-5671', '12345', '서울시 강남구 테헤란로 1', '101동 101호', 35000, ''),
       ('019a69ef-1209-72d2-a5eb-3782f9d74394', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '아이유', 'user6@example.com',
        '01012345676', '아이유 (작업실)', '010-6666-6666', '98765', '서울시 성동구 성수동', '스튜디오 301호', 120000, ''),
       ('019a69ef-1209-7000-8ca1-6118c0da4ef1', '019a698a-43ea-7c95-8b8a-e2ed1625f1f8', '이순신', 'user9@example.com',
        '01012345679', '이순신 (서울숙소)', '010-0000-0000', '56565', '서울시 동작구 노량진동', '오피스텔', 89000, ''),
       ('019a69ef-1209-7ef5-ab74-23c9c6db1b3a', '019a698a-43ea-7ba3-8634-d17b296bd88c', '박지성', 'user3@example.com',
        '01012345673', '박지성 (사무실)', '010-9876-5432', '45678', '서울시 중구 세종대로 4', '', 5000, ''),
       ('019a69ef-1209-7c25-bf97-0cd1a4297387', '019a698a-43ea-7dca-8b34-cde9a3850adb', '이영희', 'user2@example.com',
        '01012345672', '이영희 (사무실)', '010-3333-3333', '65432', '서울시 서초구 서초대로 300', '사무실 5층', 42000, ''),
       ('019a69ef-1209-7a44-a105-34f077af76b8', '019a698a-43ea-7e4a-a010-65cd58d2ccd8', '손흥민', 'user7@example.com',
        '01012345677', '손흥민', '010-1234-5677', '89012', '강원도 춘천시 8', '808동 808호', 76500, ''),
       ('019a69ef-1209-76b4-be40-2c2049a835f3', '019a698a-43ea-7785-87a6-4ba7e9e58784', '김철수', 'user1@example.com',
        '01012345671', '김철수 (회사)', '010-1111-1111', '54321', '서울시 강남구 테헤란로 200', '강남파이낸스센터', 15000, ''),
       ('019a69ef-1209-78f6-bd6b-c652586fb59d', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '아이유', 'user6@example.com',
        '01012345676', '이지은 (실명배송)', '010-8888-8888', '34343', '제주도 제주시 애월읍', '별장', 230000, ''),
       ('019a69ef-1209-708a-824e-0e46e9608bfa', '019a698a-43ea-7ba3-8634-d17b296bd88c', '박지성', 'user3@example.com',
        '01012345673', '박지성', '010-1234-5673', '34567', '경기도 수원시 영통구 3', '303동 303호', 55000, ''),
       ('019a69ef-1209-73cd-855c-4ea2bc0bb5f6', '019a698a-43ea-7c95-8b8a-e2ed1625f1f8', '이순신', 'user9@example.com',
        '01012345679', '이순신 (거제도)', '010-9999-9999', '45454', '경상남도 거제시', '조선소 근처', 19000, ''),
       ('019a69ef-1209-7c13-808b-d2aeb55c0007', '019a698a-43ea-7843-89bd-8d1b6e9a5cfe', '최민식', 'user4@example.com',
        '01012345674', '최민식', '010-1234-5674', '56789', '부산시 해운대구 5', '505동 505호', 41000, ''),
       ('019a69ef-1209-7b18-be00-6047fdb2b94c', '019a698a-43ea-7b27-9103-a70f0065184a', '김연아', 'user8@example.com',
        '01012345678', '김연아', '010-1234-5678', '90123', '경기도 군포시 9', '909동 909호', 99000, ''),
       ('019a69ef-1209-7394-81ee-148dc3539d56', '019a698a-43ea-7ba3-8634-d17b296bd88c', '박지성', 'user3@example.com',
        '01012345673', '박지성 (본사)', '010-5555-5555', '87654', '서울시 종로구 세종대로 100', '광화문빌딩', 28000, ''),
       ('019a69ef-1209-7d3a-8bbd-fa45b0c9f568', '019a698a-43ea-7f6d-b7d6-b0682abbd378', '유재석', 'user5@example.com',
        '01012345675', '유재석', '010-1234-5675', '67890', '서울시 마포구 상암동 6', '606동 606호', 33000, ''),
       ('019a69ef-1209-7b71-bbcd-641190f9341e', '019a698a-43ea-75a7-ae59-bcec4f0361ac', '아이유', 'user6@example.com',
        '01012345676', '아이유', '010-1234-5676', '78901', '서울시 강남구 청담동 7', '707동 707호', 52000, '');


-- ==========================================
-- File: 10.1.order_items.sql
-- ==========================================

CREATE TABLE order_items
(
    order_item_id     UUID PRIMARY KEY,
    order_id          UUID         NOT NULL,
    product_id        UUID,
    product_name      VARCHAR(255) NOT NULL,
    product_price     INT          NOT NULL,
    product_quantity  INT          NOT NULL,
    order_item_status VARCHAR(20)  NOT NULL,
    cancel_reason     TEXT,
    product_image_url VARCHAR(255),
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id)
        REFERENCES orders (order_id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id)
        REFERENCES products (product_id) ON DELETE SET NULL
);


-- ==========================================
-- File: 10.2.order_items-dummy.sql
-- ==========================================

INSERT INTO order_items (order_item_id,
                         order_id,
                         product_id,
                         product_name,
                         product_price,
                         product_quantity,
                         order_item_status,
                         cancel_reason)
VALUES ('019a6a12-7f22-7fec-9029-bdbb9f4aa720', '019a69ef-1209-70ef-8f92-9678c20cf1f0',
        '019a69f5-c3c0-7412-bc20-8e2345ea7203', '강철 게이밍 노트북 15인치', 1490000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7b6e-9e95-2708025a9b56', '019a69ef-1209-70ef-8f92-9678c20cf1f0',
        '019a69f5-c3c0-76ba-a0fd-3a121b5bae04', '강철 무선 이어폰', 199000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7182-8227-8c54803a52d1', '019a69ef-1209-70ef-8f92-9678c20cf1f0',
        '019a69f5-c3c0-7f83-bcef-1a508d05263f', '스마트폰 보조배터리 20000mAh', 100000, 2, 'PAID', NULL),
       ('019a6a12-7f22-71ee-abb7-bc7e1011db4f', '019a69ef-1209-72d2-a5eb-3782f9d74394',
        '019a69f5-c3c0-7caa-a08e-69d8ce1dd7cf', '영희네 여성 쉬폰 원피스', 79000, 1, 'SHIPPED', NULL),
       ('019a6a12-7f22-7d84-967c-108cbd6c36df', '019a69ef-1209-72d2-a5eb-3782f9d74394',
        '019a69f5-c3c0-7a93-8327-609bd7a9752b', '영희네 여성 트위드 자켓', 99000, 1, 'SHIPPED', NULL),
       ('019a6a12-7f22-7d0d-8c8b-663cc2fe982a', '019a69ef-1209-72d2-a5eb-3782f9d74394',
        '019a69f5-c3c0-7c23-a7dd-0c541cb65a52', '영희네 실크 스카프 (블루)', 78000, 1, 'SHIPPED', NULL),
       ('019a6a12-7f22-729b-bd24-4989c2e9fe78', '019a69ef-1209-7000-8ca1-6118c0da4ef1',
        '019a69f5-c3c0-7129-94ed-97c51816d0a2', '산소탱크 축구공 (K-리그 공인구)', 150000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7abf-b7ff-c47643bc1a08', '019a69ef-1209-7000-8ca1-6118c0da4ef1',
        '019a69f5-c3c0-7cdb-98e6-0b09a59e0f72', '산소탱크 정강이 보호대', 25000, 2, 'PAID', NULL),
       ('019a6a12-7f22-7211-b3a9-d9e7a592184f', '019a69ef-1209-7ef5-ab74-23c9c6db1b3a',
        '019a69f5-c3c0-7d83-8b89-3fb3ed04228d', '영희네 남성 옥스포드 셔츠', 59000, 1, 'CONFIRMED', NULL),
       ('019a6a12-7f22-7d45-98e3-3e9a45ce1eb2', '019a69ef-1209-7ef5-ab74-23c9c6db1b3a',
        '019a69f5-c3c0-7d7a-a40b-c8bc7dd56c27', '영희네 남성 슬랙스 (블랙)', 89000, 1, 'CONFIRMED', NULL),
       ('019a6a12-7f22-7015-8679-332fee8d479e', '019a69ef-1209-7c25-bf97-0cd1a4297387',
        '019a69f5-c3c0-7a51-8834-4e08a111fdb1', '영희네 여성 와이드 슬랙스 (베이지)', 55000, 1, 'CANCEL_PENDING', NULL),
       ('019a6a12-7f22-709f-83a8-f3a3420c8dfe', '019a69ef-1209-7c25-bf97-0cd1a4297387',
        '019a69f5-c3c0-710a-9700-a3e8d43cf1ee', '영희네 여성 블라우스 (스카이블루)', 42000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7fb6-9691-1e9b8e8dbbf2', '019a69ef-1209-7c25-bf97-0cd1a4297387',
        '019a69f5-c3c0-732f-b9bd-2a7ab2f35ba3', '영희네 여성 기본 가디건 (블랙)', 29000, 1, 'PAID', NULL),
       ('019a6a12-7f22-77ba-a260-f4c02a72893c', '019a69ef-1209-7a44-a105-34f077af76b8',
        '019a69f5-c3c0-7fa5-932a-b1b9515efa64', '산소탱크 축구화 (FG 스터드)', 180000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7d18-9bca-97a9e01e4a3c', '019a69ef-1209-76b4-be40-2c2049a835f3',
        '019a69f5-c3c0-7d26-98c2-e7e60f725c16', '강철 크롬북 11인치', 450000, 1, 'CONFIRMED', NULL),
       ('019a6a12-7f22-74c9-a2b3-db3ba861623b', '019a69ef-1209-76b4-be40-2c2049a835f3',
        '019a69f5-c3c0-7646-b57e-f56d0a09cd97', '강철 스마트폰 V1', 1200000, 1, 'CONFIRMED', NULL),
       ('019a6a12-7f22-7af6-9a8a-be7fb034298f', '019a69ef-1209-78f6-bd6b-c652586fb59d',
        '019a69f5-c3c0-73f8-a848-9378f3c1af02', '영희네 캔버스 크로스백 (아이보리)', 180000, 1, 'SHIPPED', NULL),
       ('019a6a12-7f22-7b2a-8198-f87199b064ae', '019a69ef-1209-78f6-bd6b-c652586fb59d',
        '019a69f5-c3c0-7673-a6d5-34699d4b2d60', '영희네 볼캡 (베이지)', 45000, 2, 'SHIPPED', NULL),
       ('019a6a12-7f22-7eaf-8624-b6bf17b0de49', '019a69ef-1209-78f6-bd6b-c652586fb59d',
        '019a69f5-c3c0-7d70-83ff-88279e50ec05', '영희네 패션 양말 5종 세트', 15000, 3, 'SHIPPED', NULL),
       ('019a6a12-7f22-7a52-bc8f-eb8313e5a3e9', '019a69ef-1209-708a-824e-0e46e9608bfa',
        '019a69f5-c3c0-70a0-8693-306c227d5cbe', '산소탱크 캠핑 의자 (릴렉스 체어)', 80000, 2, 'PAID', NULL),
       ('019a6a12-7f22-75e2-91a1-0d11b7104e40', '019a69ef-1209-708a-824e-0e46e9608bfa',
        '019a69f5-c3c0-7959-a603-f710fd030402', '산소탱크 캠핑용 랜턴 (LED)', 65000, 1, 'PAID', NULL),
       ('019a6a12-7f22-739d-b63d-fac84e84226d', '019a69ef-1209-73cd-855c-4ea2bc0bb5f6',
        '019a69f5-c3c0-7cf2-9f8b-69058c4e4b1c', '산소탱크 원터치 텐트 (4인용)', 250000, 1, 'CONFIRMED', NULL),
       ('019a6a12-7f22-72e6-8b12-a0c1e274218b', '019a69ef-1209-73cd-855c-4ea2bc0bb5f6',
        '019a69f5-c3c0-7bf8-9bd3-0b7eda79b84b', '산소탱크 캠핑용 침낭 (사계절)', 95000, 2, 'CONFIRMED', NULL),
       ('019a6a12-7f22-737c-9dd1-426cdadf802f', '019a69ef-1209-73cd-855c-4ea2bc0bb5f6',
        '019a69f5-c3c0-71ab-8ec9-0ab9db76a800', '산소탱크 캠핑용 코펠 (4인용)', 45000, 1, 'CONFIRMED', NULL),
       ('019a6a12-7f22-7345-a76a-42f3588e6520', '019a69ef-1209-7c13-808b-d2aeb55c0007',
        '019a69f5-c3c0-78fe-aff4-e093db2e9ae1', '강철 사무용 노트북 13인치', 890000, 1, 'PAID', NULL),
       ('019a6a12-7f22-789d-87dc-199b85569b50', '019a69ef-1209-7c13-808b-d2aeb55c0007',
        '019a69f5-c3c0-76ba-a0fd-3a121b5bae04', '강철 무선 이어폰', 199000, 1, 'PAID', NULL),
       ('019a6a12-7f22-78f7-8893-96a715ebc4a2', '019a69ef-1209-7b18-be00-6047fdb2b94c',
        '019a69f5-c3c0-7ad8-9e8e-0c415c9f544c', '영희네 남성 후드 티셔츠 (그레이)', 45000, 2, 'CANCELED', '단순 변심으로 인한 취소'),
       ('019a6a12-7f22-73f9-aaee-504131d5e567', '019a69ef-1209-7394-81ee-148dc3539d56',
        '019a69f5-c3c0-769f-b19f-89840437d235', '산소탱크 축구화 (TF 스터드)', 160000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7a43-b1c8-5df68544b99d', '019a69ef-1209-7394-81ee-148dc3539d56',
        '019a69f5-c3c0-76d2-9b87-cd61fd7c9189', '산소탱크 골키퍼 장갑 (프로용)', 75000, 1, 'PAID', NULL),
       ('019a6a12-7f22-773f-8d40-17aaf426e744', '019a69ef-1209-7394-81ee-148dc3539d56',
        '019a69f5-c3c0-7e18-bea1-e5194a9ab10e', '산소탱크 스타킹 (블랙)', 35000, 2, 'PAID', NULL),
       ('019a6a12-7f22-7702-9863-506be8a40bb0', '019a69ef-1209-7d3a-8bbd-fa45b0c9f568',
        '019a69f5-c3c0-7668-b27c-62db89013277', '영희네 남성 트레이닝 팬츠', 65000, 1, 'SHIPPED', NULL),
       ('019a6a12-7f22-7d99-b951-42efb711cefa', '019a69ef-1209-7d3a-8bbd-fa45b0c9f568',
        '019a69f5-c3c0-7a93-b5ad-d7ae6588c162', '영희네 선글라스 (블랙)', 99000, 1, 'SHIPPED', NULL),
       ('019a6a12-7f22-7cd5-9709-8f09a25644a5', '019a69ef-1209-7b71-bbcd-641190f9341e',
        '019a69f5-c3c0-7ac5-88b8-00c2d38b7dfe', '강철 태블릿 10인치', 650000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7680-843c-094b5612fa00', '019a69ef-1209-7b71-bbcd-641190f9341e',
        '019a69f5-c3c0-76ba-a0fd-3a121b5bae04', '강철 무선 이어폰', 199000, 1, 'PAID', NULL),
       ('019a6a12-7f22-7005-821a-08a78361813b', '019a69ef-1209-7b71-bbcd-641190f9341e',
        '019a69f5-c3c0-7f83-bcef-1a508d05263f', '스마트폰 보조배터리 20000mAh', 100000, 1, 'PAID', NULL);


-- ==========================================
-- File: 11.1.user_a11y_profiles.sql
-- ==========================================

CREATE TABLE user_a11y_profiles
(
    profile_id         UUID PRIMARY KEY,
    user_id            UUID                    NOT NULL,
    profile_name       VARCHAR(50)             NOT NULL,
    description        VARCHAR(200),
    is_preset          BOOLEAN   DEFAULT FALSE NOT NULL,
    contrast_level     INT                     NOT NULL,
    text_size_level    INT                     NOT NULL,
    text_spacing_level INT                     NOT NULL,
    line_height_level  INT                     NOT NULL,
    text_align         VARCHAR(10)             NOT NULL,
    screen_reader      BOOLEAN                 NOT NULL,
    smart_contrast     BOOLEAN                 NOT NULL,
    highlight_links    BOOLEAN                 NOT NULL,
    cursor_highlight   BOOLEAN                 NOT NULL,
    created_at         TIMESTAMP DEFAULT current_timestamp,
    updated_at         TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT uk_user_profile_name UNIQUE (user_id, profile_name),
    CONSTRAINT fk_user_a11y_profiles FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);


-- ==========================================
-- File: 12.1.product_ai_summary.sql
-- ==========================================

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


-- ==========================================
-- File: 13.1.refresh_token.sql
-- ==========================================

CREATE TABLE refresh_token
(
    refresh_token_id UUID PRIMARY KEY,
    user_id          UUID      NOT NULL UNIQUE,
    token            TEXT      NOT NULL,
    expiry_date      TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);


-- ==========================================
-- File: 14.1.main_page_events.sql
-- ==========================================

CREATE TABLE main_page_events
(
    event_id          UUID PRIMARY KEY,
    event_title       VARCHAR(200)  NOT NULL,
    event_description VARCHAR(1000) NOT NULL,
    event_image_url   VARCHAR(2048) NOT NULL,
    event_url         VARCHAR(2048),
    start_date        TIMESTAMP     NOT NULL,
    end_date          TIMESTAMP     NOT NULL
);

INSERT INTO main_page_events (event_id,
                              event_title,
                              event_description,
                              event_image_url,
                              event_url,
                              start_date,
                              end_date)
VALUES ('019aecba-29aa-7166-bdf1-c571a79f81ca',
        '신규 회원 환영 이벤트',
        '새로 가입한 회원들을 위한 특별 환영 이벤트! 다양한 혜택과 쿠폰을 드립니다.',
        '/a11ymarket-bucket/images/events/welcome_event_banner.png',
        '/events/welcome',
        TO_TIMESTAMP('2024-01-01', 'YYYY-MM-DD'),
        TO_TIMESTAMP('2099-12-31', 'YYYY-MM-DD')),
       ('019aecbd-61b0-7f7f-ac98-95c64c991f81',
        '무료 배송 프로모션',
        '모든 주문에 대해 무료 배송 혜택을 제공합니다. 지금 바로 쇼핑하세요!',
        '/a11ymarket-bucket/images/events/free_shipping_banner.png',
        '/events/free-shipping',
        TO_TIMESTAMP('2025-12-01', 'YYYY-MM-DD'),
        TO_TIMESTAMP('2026-06-30', 'YYYY-MM-DD')),
       ('019aecbe-3edc-73f1-a378-9347e6422e79',
        '신년 대비 건강 캠페인',
        '새해를 맞이하여 건강한 시작을 위한 특별 캠페인을 진행합니다. 건강 관련 상품들을 특별 할인된 가격에 만나보세요!',
        '/a11ymarket-bucket/images/events/health_campaign_banner.png',
        '/products?category=019a69f3-b7b4-7253-8447-2516faee63f4',
        TO_TIMESTAMP('2025-12-01', 'YYYY-MM-DD'),
        TO_TIMESTAMP('2026-01-31', 'YYYY-MM-DD'));


-- ==========================================
-- File: 15.1.views.sql
-- ==========================================

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
                   WHEN oi.order_item_status IN ('CANCELED',
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
       pi.image_url                                AS product_image_url,
       COUNT(oi.order_item_id)                     AS order_count,
       SUM(oi.product_quantity)                    AS total_quantity_sold,
       SUM(oi.product_price * oi.product_quantity) AS total_sales_amount,
       RANK()
       OVER (PARTITION BY p.seller_id
           ORDER BY SUM(oi.product_price * oi.product_quantity) DESC
           )                                       AS sales_rank
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
        ) AS (SELECT category_id,
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

