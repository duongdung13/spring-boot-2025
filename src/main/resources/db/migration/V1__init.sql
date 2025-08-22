-- Tạo bảng users mẫu
CREATE TABLE IF NOT EXISTS flyway_users (
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    full_name TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Seed dữ liệu mẫu
INSERT INTO flyway_users (email, full_name) VALUES
    ('alice@example.com', 'Alice'),
    ('bob@example.com', 'Bob')
    ON CONFLICT DO NOTHING;