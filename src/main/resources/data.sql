INSERT INTO users (user_id, name, email) VALUES
    ('a1e8f28b-9c71-4f5b-9444-8e0a29be44c0', 'Jeremy Clarkson', 'angry.bird@gmail.com'),
    ('b2f9f348-a9cb-4e99-a012-2d9f3efc8f82', 'James May', 'captain.slow@gmail.com'),
    ('c3f9d8c5-a9eb-4c3c-bb08-5c7e3f3b6a0f', 'Richard Hammond', 'hamster@topgear.com');

INSERT INTO orders (order_id, description, status, user_id) VALUES
    ('d1e8f5c6-3b2d-4f9c-a05d-76e2f543a1a1', 'Order 1 for Jeremy', 'Pending', 'a1e8f28b-9c71-4f5b-9444-8e0a29be44c0'),
    ('e2f9f8c7-6b8b-4c1a-b0e5-df7a29e2c1d2', 'Order 2 for Jeremy', 'Completed', 'a1e8f28b-9c71-4f5b-9444-8e0a29be44c0'),
    ('f3a0c4b7-9b5d-4e0a-b0a0-12345e6789b1', 'Order 1 for James', 'Shipped', 'b2f9f348-a9cb-4e99-a012-2d9f3efc8f82'),
    ('c8b1a9d4-72e8-4c3a-b0f4-2e7a3f1d8c4f', 'Order 1 for Richard', 'Delivered', 'c3f9d8c5-a9eb-4c3c-bb08-5c7e3f3b6a0f'),
    ('1e9c3f8b-4a6e-482a-9f7c-2b1d4f8a3e2d', 'Order 2 for Richard', 'Pending', 'c3f9d8c5-a9eb-4c3c-bb08-5c7e3f3b6a0f');
