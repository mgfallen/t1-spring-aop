CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE orders (
    order_id UUID PRIMARY KEY,
    description VARCHAR(255),
    status VARCHAR(50),
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
