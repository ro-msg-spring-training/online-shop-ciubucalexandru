CREATE TABLE product_category(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE supplier(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE address(
    id INT PRIMARY KEY AUTO_INCREMENT,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    county VARCHAR(100) NOT NULL,
    street VARCHAR(100) NOT NULL
);

CREATE TABLE location(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (100) NOT NULL,
    address_id INT NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE product(
    id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DECIMAL NOT NULL,
    weight DOUBLE NOT NULL,
    category_id INT,
    supplier_id INT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(category_id) REFERENCES product_category(id),
    FOREIGN KEY(supplier_id) REFERENCES supplier(id)
);

CREATE TABLE customer(
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email_address VARCHAR(100) NOT NULL
);

CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    shipped_from INT,
    customer_id INT,
    created_at TIMESTAMP,
    address_id INT,
    FOREIGN KEY(shipped_from) REFERENCES location(id),
    FOREIGN KEY(customer_id) REFERENCES customer(id),
    FOREIGN KEY(address_id) REFERENCES address(id)
);

CREATE TABLE stock(
    product_id INT,
    location_id INT,
    PRIMARY KEY(product_id, location_id),
    FOREIGN KEY(product_id) REFERENCES product(id),
    FOREIGN KEY(location_id) REFERENCES location(id),
    quantity INT NOT NULL
);

CREATE TABLE revenue(
    id INT PRIMARY KEY AUTO_INCREMENT,
    location_id INT,
    date DATE NOT NULL,
    sum DECIMAL NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE TABLE order_detail(
    order_id INT,
    product_id INT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    PRIMARY KEY (order_id, product_id),
    quantity INT NOT NULL
);











