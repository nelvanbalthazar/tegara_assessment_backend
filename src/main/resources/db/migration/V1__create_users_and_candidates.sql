CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    locked BOOLEAN DEFAULT FALSE
);

CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(50),
    experience TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
