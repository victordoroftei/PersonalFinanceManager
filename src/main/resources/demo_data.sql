DELETE FROM expenses;
DELETE FROM receipt_items;
DELETE FROM receipts;
DELETE FROM invoices
DELETE FROM notification_history;

SET @uid = (SELECT u.id FROM users u WHERE u.email = 'victordoro78@gmail.com' LIMIT 1);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent For January', 'RENT', '2023-01-05', 1500);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for February', 'RENT', '2023-02-20', 1500);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for March', 'RENT', '2023-03-05', 1500);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for April', 'RENT', '2023-04-05', 1500);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for May', 'RENT', '2023-05-05', 1500);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for June', 'RENT', '2023-06-05', 3000);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Diesel', 'FUEL', '2023-01-20', 350);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Pizza x2', 'FOOD', '2023-01-14', 70);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Winter Clothes', 'CLOTHING', '2023-02-18', 200);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'New Keyboard', 'OTHER', '2023-02-20', 500);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Transport Card', 'TRANSPORT', '2023-03-01', 120);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'New Mouse', 'OTHER', '2023-03-29', 150);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Diesel', 'FUEL', '2023-03-17', 300);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'New Clothes', 'FUEL', '2023-03-08', 100);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Gym Membership', 'OTHER', '2023-04-01', 120);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Train Tickets', 'TRANSPORT', '2023-04-07', 96.72);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Writing Utensils', 'EDUCATION', '2023-04-14', 35);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Big Gyros x2', 'FOOD', '2023-04-22', 80.50);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Bus Tickets', 'TRANSPORT', '2023-05-03', 20);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'School Tax', 'EDUCATION', '2023-05-21', 600);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Greek Salad x2', 'FOOD', '2023-05-31', 76.75);

INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Car Service', 'OTHER', '2023-06-02', 600);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Diesel', 'FUEL', '2023-06-25', 100);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Summer Clothes', 'CLOTHING', '2023-06-15', 150);


-- Normal, paid invoices:

INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'ELECTRICA FURNIZARE', 87.99, 'ELECTRICITY', '2023-05-23', '2023-06-24', '2023-05-21', '2023-06-20', 1);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Invoice - ELECTRICA FURNIZARE', 'INVOICE', '2023-06-20', 87.99);

INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'DIGI', 40, 'TELECOMMUNICATIONS', '2023-03-20', '2023-04-20', '2023-03-17', '2023-03-20', 1);
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Invoice - DIGI', 'INVOICE', '2023-03-20', 40);


-- Unpaid invoices, due in 3 daysȘ

INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'E-ON', 37.23, 'GAS', '2023-06-02', '2023-07-08', '2023-06-01', null, 0);
INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'ORANGE', 30, 'TELECOMMUNICATIONS', '2023-06-20', '2023-07-08', '2023-06-05', null, 0);


-- Unpaid invoices, due in 5 days:

INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'ELECTRICA FURNIZARE', 99.8, 'ELECTRICITY', '2023-06-05', '2023-07-10', '2023-06-03', null, 0);
INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'DIGI', 60, 'TELECOMMUNICATIONS', '2023-06-20', '2023-07-10', '2023-06-04', null, 0);


-- Unpaid invoices, due in the past (overdue):

INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'ACET', 20.5, 'WATER', '2023-06-20', '2023-07-01', '2023-06-04', null, 0);


-- Receipts:

INSERT INTO receipts(user_id, calculated_total, detected_total, retailer, image_path, receipt_date, inserted_date) VALUES (@uid, 35, null, 'KAUFLAND', null, '2023-02-22', '2023-02-22');
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Receipt', 'RECEIPT', '2023-02-22T08:32:21', 35);

INSERT INTO receipts(user_id, calculated_total, detected_total, retailer, image_path, receipt_date, inserted_date) VALUES (@uid, 80, null, 'LIDL', null, '2023-07-01', '2023-07-02');
INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Receipt', 'RECEIPT', '2023-07-02T10:22:06', 80);

SET @bid1 = 91;
SET @bid2 = @bid1 + 1;

INSERT INTO receipt_items(receipt_id, item_name, item_price) VALUES (@bid1, 'PIEPT PUI DEZOSAT 1KG', 20);
INSERT INTO receipt_items(receipt_id, item_name, item_price) VALUES (@bid1, 'PFANNER PORTOCALE 2L', 15);

INSERT INTO receipt_items(receipt_id, item_name, item_price) VALUES (@bid2, 'COZONAC BOROMIR CACAO', 20);
INSERT INTO receipt_items(receipt_id, item_name, item_price) VALUES (@bid2, 'MOZARELLA ITALIANA 500G', 40.5);
INSERT INTO receipt_items(receipt_id, item_name, item_price) VALUES (@bid2, 'NIVEA MEN SAMPON PAR', 19.5);
