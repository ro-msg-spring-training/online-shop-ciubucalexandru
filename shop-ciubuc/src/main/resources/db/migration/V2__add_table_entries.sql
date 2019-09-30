INSERT INTO address VALUES (1, 'USA', 'Newton Ridge', 'GA', 'Samuel Brassai');
INSERT INTO address VALUES (2, 'USA', 'Alcovy', 'GA', 'Teilor');
INSERT INTO address VALUES (3, 'USA', 'Oxford', 'GA', 'Baritiu');
INSERT INTO address VALUES (4, 'USA', 'Willow Woods', 'GA', 'Republicii');

INSERT INTO location VALUES (1, 'Deposit 1', 1);
INSERT INTO location VALUES (2, 'Deposit 2', 2);
INSERT INTO location VALUES (3, 'Deposit 3', 3);
INSERT INTO location VALUES (4, 'Deposit 4', 4);

INSERT INTO product_category VALUES (1, 'Electronics', 'Any device that uses electricity in some way');
INSERT INTO product_category VALUES (2, 'Sports', 'Items involved in sport activities');
INSERT INTO product_category VALUES (3, 'Peripherals', 'Devices connected to the computer via peripheral ports');

INSERT INTO supplier VALUES (1, 'SC.EMAG.SRL');
INSERT INTO supplier VALUES (2, 'SC.PC-GARAGE.SRL');

INSERT INTO customer VALUES (1, 'Bogdan Alexandru', 'Ciubuc', 'alexciubuc', '{noop}p', 'alex@ciubuc.com');
INSERT INTO customer VALUES (2, 'Horea Serban', 'Popa', 'horeapopas', '{noop}sp', 'popa@gmail.com');
INSERT INTO customer VALUES (3, 'Horea Serban', 'Popa', 'alexciubuc2', '{noop}sp', 'popa@gmail.com');

INSERT INTO product VALUES (1, 'iPhone 6S', 'The 6S variant of Apple''s smartphones.', 1400.80, 0.325, 2, 1, 'something:url/img');
INSERT INTO product VALUES (2, 'Basketball', 'A basketball designed for indoor play.', 100.00, 0.6, 2, 1, 'something2:url2/img2');
INSERT INTO product VALUES (3, 'Football', 'A football designed for outdoor play.', 50.00, 0.8, 2, 1, 'something2:url2/img2');
INSERT INTO product VALUES (4, 'Samsung Galaxy S8', 'The S8 smartphone', 3500.00, 0.4, 1, 2, 'something2:url2/img2');
INSERT INTO product VALUES (5, 'Logitech G402', 'A next-gen very precise mouse', 200.00, 0.2, 3, 2, 'something2:url2/img2');

INSERT INTO revenue VALUES (1, 1, TO_DATE('16/07/2019', 'DD/MM/YYYY'), 1000000.00);
INSERT INTO revenue VALUES (2, 2, TO_DATE('18/07/2019', 'DD/MM/YYYY'), 1230000.00);

INSERT INTO orders VALUES (1, 1, 1, '2019-07-15 17:00:09', 3);
INSERT INTO orders VALUES (2, 1, 2, '2019-07-16 12:12:09', 1);
INSERT INTO orders VALUES (3, 1, 2, '2019-07-14 15:06:09', 2);

INSERT INTO stock VALUES (1, 1, 100);
INSERT INTO stock VALUES (1, 2, 400);
INSERT INTO stock VALUES (1, 3, 200);
INSERT INTO stock VALUES (1, 4, 280);

INSERT INTO stock VALUES (2, 1, 15);
INSERT INTO stock VALUES (2, 2, 1);
INSERT INTO stock VALUES (2, 4, 20);

INSERT INTO stock VALUES (3, 2, 500);

INSERT INTO stock VALUES (4, 1, 50);

INSERT INTO stock VALUES (5, 1, 150);
INSERT INTO stock VALUES (5, 2, 150);

INSERT INTO order_detail VALUES (1, 1, 20);
INSERT INTO order_detail VALUES (2, 2, 5);
INSERT INTO order_detail VALUES (3, 1, 1);

INSERT INTO role VALUES (1, 'CUSTOMER');
INSERT INTO role VALUES (2, 'ADMIN');

INSERT INTO customer_roles VALUES (1, 1);
INSERT INTO customer_roles VALUES (1, 2);




