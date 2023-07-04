# DELETE FROM expenses;
# DELETE FROM receipt_items;
# DELETE FROM receipts;
# DELETE FROM invoices
# DELETE FROM notification_history;

SET @uid = (SELECT u.id FROM users u WHERE u.email = 'victordoro78@gmail.com' LIMIT 1);

# INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent For January', 'RENT', '2023-01-05', 1500);
# INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for February', 'RENT', '2023-02-20', 1500);
# INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for March', 'RENT', '2023-03-05', 1500);
# INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for April', 'RENT', '2023-04-05', 1500);
# INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for May', 'RENT', '2023-05-05', 1500);
# INSERT INTO expenses(user_id, description, type, expense_date, price) VALUES (@uid, 'Rent for June', 'RENT', '2023-06-05', 3000);

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

INSERT INTO invoices(user_id, retailer, amount, type, inserted_date, due_date, invoice_date, paid_date, paid) VALUES (@uid, 'ELECTRICA FURNIZARE', 87.99, 'ELECTRICITY', '2023-05-23', '2023-06-24', '2023-05-21', '2023-06-20', 1);

