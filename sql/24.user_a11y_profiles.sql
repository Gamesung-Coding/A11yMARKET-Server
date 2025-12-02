DROP TABLE user_a11y_settings CASCADE CONSTRAINTS;

CREATE TABLE user_a11y_profiles (
   profile_id         RAW(16) PRIMARY KEY,
   user_id            RAW(16) NOT NULL,
   profile_name       VARCHAR2(50) NOT NULL,
   description        VARCHAR2(200), -- 요약 텍스트
   is_preset          NUMBER(1,0) DEFAULT 0 NOT NULL, -- 0: 사용자 저장본, 1: 시스템 프리셋
   contrast_level     NUMBER(1,0) NOT NULL,
   text_size_level    NUMBER(1,0) NOT NULL,
   text_spacing_level NUMBER(1,0) NOT NULL,
   line_height_level  NUMBER(1,0) NOT NULL,
   text_align         VARCHAR2(10) NOT NULL,
   screen_reader      NUMBER(1,0) NOT NULL,
   smart_contrast     NUMBER(1,0) NOT NULL,
   highlight_links    NUMBER(1,0) NOT NULL,
   cursor_highlight   NUMBER(1,0) NOT NULL,
   created_at         TIMESTAMP DEFAULT systimestamp,
   updated_at         TIMESTAMP DEFAULT systimestamp,
   CONSTRAINT uk_user_profile_name UNIQUE ( user_id,
                                            profile_name )
);