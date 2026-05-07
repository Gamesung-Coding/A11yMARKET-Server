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
