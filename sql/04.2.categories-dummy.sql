INSERT INTO categories (
    category_id,
    parent_cat_id,
    category_name
)
VALUES
    ('019a69f3-b7b4-74b4-902d-651d1f11d323', NULL, '디지털/가전'),
    ('019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', NULL, '패션/의류'),
    ('019a69f3-b7b4-7253-8447-2516faee63f4', NULL, '스포츠/레저'),
    ('019a69f3-b7b4-79cd-a461-61ee136c6801', '019a69f3-b7b4-74b4-902d-651d1f11d323', '노트북'),
    ('019a69f3-b7b4-7a8f-ba32-343d5068fae9', '019a69f3-b7b4-74b4-902d-651d1f11d323', '모바일'),
    ('019a69f3-b7b4-71ae-9dff-aa18f0005106', '019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', '남성의류'),
    ('019a69f3-b7b4-743b-b816-e3a6973b47d1', '019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', '여성의류'),
    ('019a69f3-b7b4-711e-8cc7-eba3d7f29fdf', '019a69f3-b7b4-7c2d-afb2-2fcb168bfe12', '패션잡화'),
    ('019a69f3-b7b4-76b3-b1de-1994732363db', '019a69f3-b7b4-7253-8447-2516faee63f4', '캠핑용품'),
    ('019a69f3-b7b4-7e4e-83b7-394018b33917', '019a69f3-b7b4-7253-8447-2516faee63f4', '축구용품');
