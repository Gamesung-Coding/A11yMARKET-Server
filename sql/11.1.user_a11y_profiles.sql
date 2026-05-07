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
