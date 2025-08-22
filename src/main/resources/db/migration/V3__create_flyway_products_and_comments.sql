-- Create flyway_products table
CREATE TABLE IF NOT EXISTS flyway_products (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create flyway_product_comments table
CREATE TABLE IF NOT EXISTS flyway_product_comments (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    author_name TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_product_comments_product 
        FOREIGN KEY (product_id) REFERENCES flyway_products(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_flyway_product_comments_product_id ON flyway_product_comments(product_id);

-- Insert sample data
INSERT INTO flyway_products (name, description, price, stock) VALUES
    ('iPhone 15', 'Latest iPhone model', 999.99, 50),
    ('Samsung Galaxy S24', 'Premium Android phone', 899.99, 30)
    ON CONFLICT DO NOTHING;

INSERT INTO flyway_product_comments (product_id, comment, author_name) VALUES
    (1, 'Great phone with excellent camera quality!', 'John Doe'),
    (1, 'Battery life could be better', 'Jane Smith'),
    (2, 'Love the display quality', 'Mike Johnson')
    ON CONFLICT DO NOTHING;